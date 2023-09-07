package com.accWH.accWH.controller;

import com.accWH.accWH.model.Expert;
import com.accWH.accWH.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class HomeController {
    @Autowired
    private ExpertRepository expertRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Expert> experts = expertRepository.findAll();
        model.addAttribute("experts", experts);
        return "expert/list";
    }
}
