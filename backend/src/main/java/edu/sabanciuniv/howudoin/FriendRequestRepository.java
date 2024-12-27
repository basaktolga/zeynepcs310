package edu.sabanciuniv.howudoin;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    List<FriendRequest> findByReceiverUsernameAndStatus(String receiverUsername, String status);
    List<FriendRequest> findBySenderUsernameAndStatus(String senderUsername, String status);
    List<FriendRequest> findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);
}
