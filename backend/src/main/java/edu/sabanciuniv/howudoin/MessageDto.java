package edu.sabanciuniv.howudoin;

public class MessageDto {
    private String receiverUsername;
    private String content;

    public String getReceiverUsername() { return receiverUsername; }
    public void setReceiverUsername(String receiverUsername) { this.receiverUsername = receiverUsername; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}

