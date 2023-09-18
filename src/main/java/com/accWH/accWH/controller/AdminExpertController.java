package com.accWH.accWH.controller;

import com.accWH.accWH.model.Expert;
import com.accWH.accWH.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/expert")
public class AdminExpertController {

    @Autowired
    private ExpertRepository expertRepository;

    @GetMapping
    public String listExperts(Model model) {
        List<Expert> experts = expertRepository.findAll();
        model.addAttribute("experts", experts);
        return "list";
    }

    @GetMapping("/add")
    public String addExpertForm(Model model) {
        Expert expert = new Expert();
        model.addAttribute("expert", expert);
        return "add";
    }

    @PostMapping("/add")
    public String addExpert(@ModelAttribute Expert expert) {
        expertRepository.save(expert);
        return "redirect:expert/list";
    }

    @GetMapping("/{expertId}/edit")
    public String editExpertForm(@PathVariable Long expertId, Model model) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        model.addAttribute("expert", expert);
        return "edit";
    }

    @PostMapping("/{expertId}/edit")
    public String editExpert(@PathVariable Long expertId, @ModelAttribute Expert expert) {
        Expert existingExpert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        existingExpert.setFname(expert.getFname());
        existingExpert.setLname(expert.getLname());
        existingExpert.setDep(expert.getDep());

        expertRepository.save(existingExpert);
        return "redirect:expert/list";
    }

    @GetMapping("/{expertId}/delete")
    public String deleteExpert(@PathVariable Long expertId) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        expertRepository.delete(expert);
        return "redirect:expert/list";
    }

}
