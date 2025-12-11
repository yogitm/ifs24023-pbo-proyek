package com.example.tonemaster.controller;

import com.example.tonemaster.entity.User;
import com.example.tonemaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 1. Tampilkan Halaman Login
    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    // 2. Tampilkan Halaman Register
    @GetMapping("/register")
    public String viewRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // 3. Proses Register User Baru
    @PostMapping("/register/save")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login?success"; // Balik ke login kalau sukses
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Balik ke form register kalau gagal
        }
    }
}