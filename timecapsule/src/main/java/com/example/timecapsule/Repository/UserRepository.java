package com.example.timecapsule.Repository;

import com.example.timecapsule.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    Optional<User> findById(Long Id);
    User findByEmail(String email);

}


