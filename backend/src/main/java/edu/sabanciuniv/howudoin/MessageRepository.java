package edu.sabanciuniv.howudoin;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    @Query("{ $or: [ " +
           "{ 'senderUsername': ?0, 'receiverUsername': ?1 }, " +
           "{ 'senderUsername': ?1, 'receiverUsername': ?0 } " +
           "] }")
    List<Message> findMessagesBetweenUsers(String user1, String user2);
}