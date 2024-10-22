package com.example.timecapsule.Service;

import com.example.timecapsule.Entity.Message;
import com.example.timecapsule.Entity.User;
import com.example.timecapsule.Repository.MessageRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private UserService userService;

    private SecretKey aesKey;


    public SecretKey getAESKey() {
        System.out.println(aesKey + " 1");
        return aesKey;
    }

    public void saveMessage(Message message) {
        //enkrypting the message before saving
        try {
           String encryptedText = encryptionService.encrypt(message.getMessage());
           message.setMessage(encryptedText);
           messageRepository.save(message);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error with message");
        }
    }
    //gets and decrypt messages by senders name
    public List<String> getAllMessagesByEmail(String email) {
        User user = userService.findByEmail(email); // Get user by email
        if (user == null) {
            return new ArrayList<>(); // Return an empty list if user is not found
        }

        List<Message> messages = messageRepository.findAllByUserEmail(email); // Fetch messages by user email
        List<String> decryptedMessages = new ArrayList<>();

        for (Message msg : messages) {
            try {
                String decryptedText = encryptionService.decrypt(msg.getMessage());
                decryptedMessages.add(decryptedText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return decryptedMessages;
    }


//    public List<Message> getAllMessageByEmail(String email) {
//        List<Message> messages = messageRepository.findAllByEmail(email);
//        List<Message> decryptedMessages = new ArrayList<>();
//
//        for (Message msg : messages) {
//            try {
//                String decryptedText = encryptionService.decrypt(msg.getMessage(), aesKey);
//                msg.setMessage(decryptedText);
//                decryptedMessages.add(msg);
//                System.out.println("encryption success" + " " + aesKey);
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("decryption failed" + msg.getMessage());
//            }
//        }
//
//        return decryptedMessages;
//    }

}
