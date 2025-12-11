package com.example.tonemaster.service;

import com.example.tonemaster.entity.User;
import com.example.tonemaster.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("baru");
        user.setPassword("pass");

        Mockito.when(userRepository.findByUsername("baru")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        userService.registerUser(user);

        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals("encodedPass", user.getPassword());
        Assertions.assertEquals("ADMIN", user.getRole());
    }

    @Test
    public void testRegisterUser_Fail_UsernameExists() {
        User user = new User();
        user.setUsername("lama");

        Mockito.when(userRepository.findByUsername("lama")).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.registerUser(user);
        });
    }
}