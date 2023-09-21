package com.accWH.accWH.controller;

import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/expert")
public class AdminExpertController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public String userList(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/home/userList";
    }
    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/home/registration";
    }

    @PostMapping("/registration")
    public String registrationSubmit(@ModelAttribute User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/admin/expert/list";
    }
    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID пользователя:" + id));

        model.addAttribute("user", user);
        return "admin/home/userEdit";
    }

    @PostMapping("/{id}/edit")
    public String editUser(@PathVariable Long id, @ModelAttribute User editedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID пользователя:" + id));

        existingUser.setFname(editedUser.getFname());
        existingUser.setLname(editedUser.getLname());
        existingUser.setDep(editedUser.getDep());
        existingUser.setUsername(editedUser.getUsername());
        existingUser.setPassword(passwordEncoder.encode(editedUser.getPassword()));
        userRepository.save(existingUser);
        return "redirect:/admin/expert/list";
    }
    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID пользователя:" + id));

        userRepository.delete(user);

        return "redirect:/admin/expert/list";
    }

}
