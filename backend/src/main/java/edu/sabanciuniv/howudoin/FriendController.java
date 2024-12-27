package edu.sabanciuniv.howudoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public void sendFriendRequest(@RequestHeader("Authorization") String authorizationHeader,
                                  @RequestBody FriendRequestDto requestDto) {
        System.out.println("Received friend request: " + requestDto.getSenderUsername() + " -> " + requestDto.getReceiverUsername());
        // Extract username (email) from JWT
        String authenticatedUserName = jwtUtil.extractUsername(authorizationHeader.substring(7));
        System.out.println("Authenticated user: " + authenticatedUserName);

        // Ensure the authenticated user is the sender
        if (!authenticatedUserName.equals(requestDto.getSenderUsername())) {
            throw new UnauthorizedException("You can only send friend requests on your behalf.");
        }

        // Proceed to send the friend request
        friendService.sendFriendRequest(authorizationHeader.substring(7), requestDto.getReceiverUsername());
    }

    @PostMapping("/accept")
    public void acceptFriendRequest(@RequestHeader("Authorization") String authorizationHeader,
                                    @RequestBody FriendRequestDto requestDto) {
        //Extract JWT from Authorization header (remove "Bearer ")
        String jwt = authorizationHeader.substring(7);

        // Extract username/email from JWT
        String username = jwtUtil.extractUsername(jwt);

        // Validate the email/username corresponds to the receiver of the friend request
        if (!username.equals(requestDto.getReceiverUsername())) {
            throw new UnauthorizedException("You can only accept friend requests on your behalf.");
        }

        friendService.acceptFriendRequest(jwt, requestDto.getSenderUsername());
    }

    @GetMapping
    public ResponseEntity<List<String>> getFriends(@RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        System.out.println("Getting friends for user: " + username); // Debug log
        List<String> friends = friendService.getFriends(username);  // Changed from listFriends to getFriends
        System.out.println("Found friends: " + friends); // Debug log
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequest>> getPendingRequests(@RequestHeader("Authorization") String authorizationHeader) {
        String jwt = authorizationHeader.substring(7); // Remove "Bearer "
        String username = jwtUtil.extractUsername(jwt);
        List<FriendRequest> requests = friendService.getPendingRequests(username);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestBody FriendRequestDto requestDto) {
        String jwt = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        
        if (!username.equals(requestDto.getReceiverUsername())) {
            throw new UnauthorizedException("You can only reject requests sent to you");
        }
        
        friendService.rejectFriendRequest(requestDto.getSenderUsername(), username);
        return ResponseEntity.ok().build();
    }
}
