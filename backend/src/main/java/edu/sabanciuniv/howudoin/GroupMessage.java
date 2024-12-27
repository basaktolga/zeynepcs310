package edu.sabanciuniv.howudoin;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "groupMessages")
public class GroupMessage {
    @Id
    private String id;
    private String groupId;
    private String senderUsername;
    private String content;
    private Date timestamp;

    // Constructors
    public GroupMessage() {
    }

    public GroupMessage(String groupId, String senderUsername, String content) {
        this.groupId = groupId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.timestamp = new Date();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}