package com.accWH.accWH.controller;

import com.accWH.accWH.model.Expert;
import com.accWH.accWH.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/select-expert")
public class ExpertSelectionController {

    @Autowired
    private ExpertRepository expertRepository;

    @GetMapping
    public String selectExpertForm(Model model) {
        List<Expert> experts = expertRepository.findAll();
        model.addAttribute("experts", experts);
        return "admin/expert/select";
    }

    @PostMapping
    public String selectExpert(@RequestParam("expertId") Long expertId) {
        return "redirect:admin/expert/" + expertId;
    }
}
