package com.example.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.User;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username); // optional, useful for validation
}
