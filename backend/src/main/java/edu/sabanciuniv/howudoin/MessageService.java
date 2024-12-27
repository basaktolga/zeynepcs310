package edu.sabanciuniv.howudoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Message sendMessage(String senderUsername, String receiverUsername, String content) {
        // Validate users exist
        if (!userRepository.findByEmail(receiverUsername).isPresent()) {
            throw new RuntimeException("Receiver not found");
        }
        
        Message message = new Message(senderUsername, receiverUsername, content);
        return messageRepository.save(message);
    }

    public List<Message> getMessagesBetweenUsers(String user1, String user2) {
        return messageRepository.findMessagesBetweenUsers(user1, user2);
    }
}