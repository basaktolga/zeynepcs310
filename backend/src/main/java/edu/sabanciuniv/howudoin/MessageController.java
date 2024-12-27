package edu.sabanciuniv.howudoin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MessageDto messageDto) {
        String jwt = authHeader.substring(7);
        String senderUsername = jwtUtil.extractUsername(jwt);
        Message message = messageService.sendMessage(senderUsername, messageDto.getReceiverUsername(), messageDto.getContent());
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{otherUsername}")
    public ResponseEntity<List<Message>> getMessages(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String otherUsername) {
        String jwt = authHeader.substring(7);
        String currentUsername = jwtUtil.extractUsername(jwt);
        List<Message> messages = messageService.getMessagesBetweenUsers(currentUsername, otherUsername);
        return ResponseEntity.ok(messages);
    }
}
