package com.example.tonemaster.config;

import com.example.tonemaster.entity.User;
import com.example.tonemaster.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DataSeederTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataSeeder dataSeeder;

    // 1. Skenario: Admin BELUM ada (Harus buat baru) -> Ini yang bikin Hijau baris IF
    @Test
    public void testRun_AdminNotExists() throws Exception {
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("admin123")).thenReturn("encodedPass");

        dataSeeder.run();

        // Pastikan save dipanggil
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    // 2. Skenario: Admin SUDAH ada (Jangan buat baru) -> Ini yang bikin Hijau baris ELSE (kuningnya ilang)
    @Test
    public void testRun_AdminExists() throws Exception {
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(new User()));

        dataSeeder.run();

        // Pastikan save TIDAK dipanggil
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }
}