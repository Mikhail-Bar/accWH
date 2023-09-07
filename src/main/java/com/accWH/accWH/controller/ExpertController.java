package com.accWH.accWH.controller;

import com.accWH.accWH.model.Expert;
import com.accWH.accWH.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/experts")
public class ExpertController {
    @Autowired
    private ExpertRepository expertRepository;

    @GetMapping
    public String listExperts(Model model) {
        List<Expert> experts = expertRepository.findAll();
        model.addAttribute("experts", experts);
        return "expert/list";
    }

    @GetMapping("/add")
    public String addExpertForm(Model model) {
        Expert expert = new Expert();
        model.addAttribute("expert", expert);
        return "expert/add";
    }

    @PostMapping("/add")
    public String addExpert(@ModelAttribute Expert expert) {
        expertRepository.save(expert);
        return "redirect:/experts";
    }

    @GetMapping("/{expertId}/edit")
    public String editExpertForm(@PathVariable Long expertId, Model model) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expert Id:" + expertId));

        model.addAttribute("expert", expert);
        return "expert/edit";
    }

    @PostMapping("/{expertId}/edit")
    public String editExpert(@PathVariable Long expertId, @ModelAttribute Expert expert) {
        Expert existingExpert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expert Id:" + expertId));

        existingExpert.setFname(expert.getFname());
        expertRepository.save(existingExpert);
        return "redirect:/experts";
    }

    @GetMapping("/{expertId}/delete")
    public String deleteExpert(@PathVariable Long expertId) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expert Id:" + expertId));

        expertRepository.delete(expert);
        return "redirect:/experts";
    }
}
