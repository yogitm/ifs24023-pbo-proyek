package com.example.tonemaster.service;

import com.example.tonemaster.entity.User;
import com.example.tonemaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username sudah terpakai!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ADMIN"); 
        userRepository.save(user);
    }
}