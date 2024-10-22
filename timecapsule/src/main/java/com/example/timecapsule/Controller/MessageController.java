package com.example.timecapsule.Controller;

import com.example.timecapsule.Entity.Message;
import com.example.timecapsule.Entity.User;
import com.example.timecapsule.Service.MessageService;
import com.example.timecapsule.Service.UserService;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")

public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    private static final String SECRET = "RmV2dDJDZzJ5MkVma1B4R3lNdE1qYzBHRnBzYklBUTA=";

    //sends a new message
    @PostMapping("/save")
    public ResponseEntity sendMessage(@RequestBody Message message, @RequestHeader("Authorization") String token) throws Exception {
        String email = getEmailFromToken(token); //get the email from token
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body(null);
        }

        message.setUser(user); //user in the message
        messageService.saveMessage(message);
        //System.out.println("Message sent: " + savedMessage);
        return ResponseEntity.ok(message);
    }

    //get messages by email
    @GetMapping("/fetch/{email}")
    public ResponseEntity<List<String>> getAllMessage(@PathVariable String email) throws Exception {
        List<String> messages = messageService.getAllMessagesByEmail(email);
        System.out.println("Fetching messages for email: " + email);

        if (messages.isEmpty()) {
            System.out.println("No messages found for email: " + email);
        }
        return ResponseEntity.ok(messages);
    }
    private String getEmailFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token.replace("Bearer ","").trim());
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims.getSubject(); // this return the users email
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
