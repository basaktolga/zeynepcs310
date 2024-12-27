package edu.sabanciuniv.howudoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GroupDto groupDto) {
        String jwt = authHeader.substring(7); // Remove "Bearer "
        Group group = groupService.createGroup(jwt, groupDto.getName(), groupDto.getMemberUsernames());
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<?> addMember(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String groupId,
            @RequestBody String memberUsername) {
        String jwt = authHeader.substring(7);
        groupService.addMemberToGroup(jwt, groupId, memberUsername);
        return ResponseEntity.ok("Member added successfully.");
    }

    @PostMapping("/{groupId}/send-message")
    public ResponseEntity<?> sendMessage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String groupId,
            @RequestBody GroupMessageDto messageDto) {
        String jwt = authHeader.substring(7);
        groupService.sendMessageToGroup(jwt, groupId, messageDto.getContent());
        return ResponseEntity.ok("Message sent successfully.");
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<GroupMessage>> getMessages(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String groupId) {
        String jwt = authHeader.substring(7);
        List<GroupMessage> messages = groupService.getGroupMessages(jwt, groupId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<String>> getMembers(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String groupId) {
        String jwt = authHeader.substring(7);
        List<String> groupMembers = groupService.getGroupMembers(jwt, groupId);

        return ResponseEntity.ok(groupMembers);
    }

    @GetMapping
    public ResponseEntity<List<Group>> getGroups(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String jwt = authHeader.substring(7); // Remove "Bearer "
            List<Group> groups = groupService.getUserGroupsWithMembers(jwt);
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}