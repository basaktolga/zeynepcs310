package edu.sabanciuniv.howudoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, GroupMessageRepository groupMessageRepository, JwtUtil jwtUtil, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMessageRepository = groupMessageRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public Group createGroup(String jwt, String name, List<String> memberUsernames) {
        String creatorUsername = jwtUtil.extractUsername(jwt);

        if (!memberUsernames.contains(creatorUsername)) {
            memberUsernames.add(creatorUsername);
        }

        for (String memberUsername : memberUsernames) {
            boolean userExists = userRepository.findByEmail(memberUsername).isPresent();
            if (!userExists) {
                throw new IllegalArgumentException("User with username " + memberUsername + " does not exist.");
            }
        }

        Group group = new Group(name, memberUsernames);
        return groupRepository.save(group);
    }

    public void addMemberToGroup(String jwt, String groupId, String memberUsername) {
        String requestingUsername = jwtUtil.extractUsername(jwt);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        boolean userExists = userRepository.findByEmail(memberUsername).isPresent();
        if (!userExists) {
            throw new IllegalArgumentException("User with the given username does not exist.");
        }

        if (!group.getMemberUsernames().contains(requestingUsername)) {
            throw new UnauthorizedException("Only group members can add others.");
        }

        if (group.getMemberUsernames().contains(memberUsername)) {
            throw new IllegalStateException("User is already a member of the group.");
        }

        group.addMember(memberUsername);
        groupRepository.save(group);
    }

    public void sendMessageToGroup(String jwt, String groupId, String content) {
        String senderUsername = jwtUtil.extractUsername(jwt);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getMemberUsernames().contains(senderUsername)) {
            throw new UnauthorizedException("Only group members can send messages.");
        }
        GroupMessage groupMessage = new GroupMessage(groupId, senderUsername, content);
        groupMessageRepository.save(groupMessage);
    }

    public List<GroupMessage> getGroupMessages(String jwt, String groupId) {
        String requestingUsername = jwtUtil.extractUsername(jwt);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getMemberUsernames().contains(requestingUsername)) {
            throw new UnauthorizedException("Only group members can view messages.");
        }
        return groupMessageRepository.findByGroupId(groupId);
    }

    public List<String> getGroupMembers(String jwt, String groupId) {
        String requestingUsername = jwtUtil.extractUsername(jwt);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getMemberUsernames().contains(requestingUsername)) {
            throw new UnauthorizedException("Only group members can view the list of members in the group.");
        }

        return group.getMemberUsernames();
    }

    public List<Group> getUserGroups(String jwt) {
        String username = jwtUtil.extractUsername(jwt);
        return groupRepository.findAll().stream()
                .filter(group -> group.getMemberUsernames().contains(username))
                .collect(Collectors.toList());
    }

    public List<Group> getUserGroupsWithMembers(String jwt) {
        try {
            String username = jwtUtil.extractUsername(jwt);
            List<Group> userGroups = groupRepository.findAll().stream()
                    .filter(group -> group.getMemberUsernames().contains(username))
                    .collect(Collectors.toList());
            
            System.out.println("Found " + userGroups.size() + " groups for user: " + username);
            return userGroups;
        } catch (Exception e) {
            System.err.println("Error fetching groups: " + e.getMessage());
            throw new RuntimeException("Error fetching user groups", e);
        }
    }
}
