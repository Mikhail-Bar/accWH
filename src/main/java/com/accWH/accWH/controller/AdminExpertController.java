package com.accWH.accWH.controller;

import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/expert")
public class AdminExpertController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listExperts(Model model) {
        List<User> experts = userRepository.findAll();
        model.addAttribute("experts", experts);
        return "admin/expert/list";
    }

    @GetMapping("/add")
    public String addExpertForm(Model model) {
       User expert = new User();
        model.addAttribute("expert", expert);
        return "admin/expert/add";
    }

    @PostMapping("/add")
    public String addExpert(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:admin/expert/list";
    }

    @GetMapping("/{expertId}/edit")
    public String editExpertForm(@PathVariable Long expertId, Model model) {
        User user = userRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        model.addAttribute("expert", user);
        return "admin/expert/edit";
    }

    @PostMapping("/{expertId}/edit")
    public String editExpert(@PathVariable Long expertId, @ModelAttribute User user) {
        User existingExpert = userRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        existingExpert.setFname(user.getFname());
        existingExpert.setLname(user.getLname());
        existingExpert.setDep(user.getDep());

        userRepository.save(existingExpert);
        return "redirect:admin/expert/list";
    }

    @GetMapping("/{expertId}/delete")
    public String deleteExpert(@PathVariable Long expertId) {
        User expert = userRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        userRepository.delete(expert);
        return "redirect:admin/expert/list";
    }

}
