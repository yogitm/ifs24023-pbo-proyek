package com.example.tonemaster.config;

import com.example.tonemaster.entity.User;
import com.example.tonemaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Cek jika admin belum ada, maka buat baru
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Passwordnya: admin123
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("✅ User ADMIN berhasil dibuat: admin / admin123");
        }
    }
}