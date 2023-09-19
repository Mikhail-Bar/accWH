package com.accWH.accWH.controller;

import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String registrationForm(Model model) {
        List<User> experts = userRepository.findAll();
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping
    public String registrationSubmit(@ModelAttribute User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/login";
    }
    @GetMapping("/experts")
    public String listExperts(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("experts", users);
        return "expert/list";
    }
}
