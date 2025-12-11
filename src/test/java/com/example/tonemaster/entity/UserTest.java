package com.example.tonemaster.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void testGetterSetter() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("pass");
        user.setRole("ADMIN");

        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("test", user.getUsername());
        Assertions.assertEquals("pass", user.getPassword());
        Assertions.assertEquals("ADMIN", user.getRole());
    }
}