package com.example.timecapsule.Repository;

import com.example.timecapsule.Entity.Message;
import com.example.timecapsule.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByUserEmail(String email);
   // List<Message> findAllByUser(Optional<User> user);

}