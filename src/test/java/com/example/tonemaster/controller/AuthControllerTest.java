package com.example.tonemaster.controller;

import com.example.tonemaster.entity.User;
import com.example.tonemaster.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // PENTING

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testViewLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testViewRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        Mockito.doNothing().when(userService).registerUser(Mockito.any(User.class));

        mockMvc.perform(post("/register/save")
                .param("username", "newuser")
                .param("password", "pass")
                .with(csrf())) // PENTING
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?success"));
    }

    @Test
    public void testRegisterUser_Fail() throws Exception {
        Mockito.doThrow(new RuntimeException("User exists")).when(userService).registerUser(Mockito.any(User.class));

        mockMvc.perform(post("/register/save")
                .param("username", "existing")
                .param("password", "pass")
                .with(csrf())) // PENTING
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }
}