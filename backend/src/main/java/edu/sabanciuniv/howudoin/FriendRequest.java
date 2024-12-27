package edu.sabanciuniv.howudoin;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "friendRequests")
public class FriendRequest {
    @Id
    private String id;
    private String senderUsername;
    private String receiverUsername;
    private String status;  // pending/accepted/rejected
    private LocalDateTime timestamp;

    // Constructors
    public FriendRequest() {} // Default constructor

    public FriendRequest(String senderUsername, String receiverId) { // Parametric constructor
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverId;
        this.status = "pending";
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSenderUsername(String senderId) {
        this.senderUsername = senderId;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
