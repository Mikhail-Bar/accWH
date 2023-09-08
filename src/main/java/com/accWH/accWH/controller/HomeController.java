package com.accWH.accWH.controller;

import com.accWH.accWH.model.Expert;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Controller
public class HomeController {
    @Autowired
    private ExpertRepository expertRepository;
    @Autowired
    private CertificateRepository certificateRepository;


    @GetMapping("/")
    public String home(Model model) {
        List<Expert> experts = expertRepository.findAll();
        model.addAttribute("experts", experts);
        return "home/expertHome";
    }
}
