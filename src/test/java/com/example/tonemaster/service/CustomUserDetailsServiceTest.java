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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    // 1. Test jika User Ditemukan (Success)
    @Test
    public void testLoadUserByUsername_Success() {
        // Data Pura-pura
        User dummyUser = new User();
        dummyUser.setUsername("admin");
        dummyUser.setPassword("pass123");
        dummyUser.setRole("ADMIN");

        // Ajari Mockito: Kalau cari "admin", kasih dummyUser ini
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(dummyUser));

        // Jalankan method aslinya
        UserDetails result = service.loadUserByUsername("admin");

        // Cek hasilnya
        Assertions.assertNotNull(result);
        Assertions.assertEquals("admin", result.getUsername());
        Assertions.assertEquals("pass123", result.getPassword());
    }

    // 2. Test jika User Tidak Ditemukan (Error)
    @Test
    public void testLoadUserByUsername_NotFound() {
        // Ajari Mockito: Kalau cari "hantu", kasih kosong (Optional.empty)
        Mockito.when(userRepository.findByUsername("hantu")).thenReturn(Optional.empty());

        // Pastikan dia melempar Error UsernameNotFoundException
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("hantu");
        });
    }
}