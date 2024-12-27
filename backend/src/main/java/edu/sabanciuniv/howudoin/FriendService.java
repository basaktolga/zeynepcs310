package edu.sabanciuniv.howudoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public void sendFriendRequest(String jwt, String receiverUsername) {
        // Extract userId from JWT
        String senderUsername = jwtUtil.extractUsername(jwt);

        // Check if the friend request already exists
        if (!friendRequestRepository.findBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername).isEmpty()) {
            throw new RuntimeException("Friend request already sent.");
        }

        // Validate the receiver exists
        Optional<User> receiver = userRepository.findByEmail(receiverUsername);
        if (receiver.isEmpty()) {
            throw new RuntimeException("Receiver not found.");
        }

        // Create and save a new friend request
        FriendRequest request = new FriendRequest(senderUsername, receiverUsername);
        friendRequestRepository.save(request);
    }

    public void acceptFriendRequest(String jwt, String senderUsername) {
        // Extract receiverId from JWT
        String receiverUsername = jwtUtil.extractUsername(jwt);

        // Retrieve all requests between the two users (both directions)
        List<FriendRequest> requests = friendRequestRepository.findBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername);
        requests.addAll(friendRequestRepository.findBySenderUsernameAndReceiverUsername(receiverUsername, senderUsername));

        // Filter the list to get the first pending request
        Optional<FriendRequest> pendingRequest = requests.stream()
                .filter(request -> "pending".equals(request.getStatus()))
                .findFirst();

        if (pendingRequest.isEmpty()) {
            throw new RuntimeException("No pending friend request between these users.");
        }

        // Accept the first pending request
        FriendRequest requestToAccept = pendingRequest.get();
        requestToAccept.setStatus("accepted");
        friendRequestRepository.save(requestToAccept);

        // Remove all remaining friend requests between these users
        List<FriendRequest> duplicateRequests = requests.stream()
                .filter(request -> request != requestToAccept)  // Exclude the accepted request
                .toList();
        friendRequestRepository.deleteAll(duplicateRequests);

        // Add each user to the other’s friends list
        Optional<User> sender = userRepository.findByEmail(senderUsername);
        Optional<User> receiver = userRepository.findByEmail(receiverUsername);

        if (sender.isPresent() && receiver.isPresent()) {
            User senderUser = sender.get();
            User receiverUser = receiver.get();

            senderUser.getFriends().add(receiverUsername);
            receiverUser.getFriends().add(senderUsername);

            userRepository.save(senderUser);
            userRepository.save(receiverUser);
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    public List<String> getFriends(String username) {
        System.out.println("FriendService: Getting friends for " + username); // Debug log
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isPresent()) {
            User userObj = user.get();
            List<String> friends = userObj.getFriends();
            System.out.println("FriendService: Found friends: " + friends); // Debug log
            return friends;
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }

    public List<FriendRequest> getPendingRequests(String username) {
        return friendRequestRepository.findByReceiverUsernameAndStatus(username, "pending");
    }

    public void rejectFriendRequest(String senderUsername, String receiverUsername) {
        List<FriendRequest> requests = friendRequestRepository.findBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername);
        FriendRequest request = requests.stream()
                .filter(r -> "pending".equals(r.getStatus()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No pending friend request found"));
                
        friendRequestRepository.delete(request);
    }
}
