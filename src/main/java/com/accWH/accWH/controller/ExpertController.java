package com.accWH.accWH.controller;

import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/experts")
public class ExpertController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listExperts(Model model) {
        List<User> experts = userRepository.findAll();
        model.addAttribute("experts", experts);
        return "expert/list";
    }

    @GetMapping("/add")
    public String addExpertForm(Model model) {
        User user = new User();
        model.addAttribute("expert", user);
        return "expert/add";
    }

    @PostMapping("/add")
    public String addExpert(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/experts";
    }

    @GetMapping("/{expertId}/edit")
    public String editExpertForm(@PathVariable Long expertId, Model model) {
        User user = userRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        model.addAttribute("expert", user);
        return "expert/edit";
    }

    @PostMapping("/{expertId}/edit")
    public String editExpert(@PathVariable Long expertId, @ModelAttribute User user) {
        User existingExpert = userRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        existingExpert.setFname(user.getFname());
        existingExpert.setLname(user.getLname());
        existingExpert.setDep(user.getDep());

        userRepository.save(existingExpert);
        return "redirect:/experts";
    }

    @GetMapping("/{expertId}/delete")
    public String deleteExpert(@PathVariable Long expertId) {
        User user = userRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        userRepository.delete(user);
        return "redirect:/experts";
    }
   /* @GetMapping("/{id}")
    public String getExpert(@PathVariable("id") Long id, Model model, Authentication authentication) {
        String username = authentication.getName();

        Optional<Expert> optionalExpert = expertRepository.findByUsername(username);

        if (optionalExpert.isPresent()) {
            Expert expert = optionalExpert.get();
            model.addAttribute("expert", expert);
        } else {
            model.addAttribute("expert", null);
        }

        return "expert/expertHome";
    }*/

}
