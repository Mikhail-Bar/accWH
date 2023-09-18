package com.accWH.accWH.controller;

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
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        model.addAttribute("expert", expert);
        return "expert/edit";
    }

    @PostMapping("/{expertId}/edit")
    public String editExpert(@PathVariable Long expertId, @ModelAttribute Expert expert) {
        Expert existingExpert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        existingExpert.setFname(expert.getFname());
        existingExpert.setLname(expert.getLname());
        existingExpert.setDep(expert.getDep());

        expertRepository.save(existingExpert);
        return "redirect:/experts";
    }

    @GetMapping("/{expertId}/delete")
    public String deleteExpert(@PathVariable Long expertId) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный id эксперта:" + expertId));

        expertRepository.delete(expert);
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
