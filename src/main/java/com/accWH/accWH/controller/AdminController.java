package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.UserRepository;
import com.accWH.accWH.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CertificateService certificateService; // Ваш сервис

    @Autowired
    private UserRepository userRepository; // Ваш репозиторий для пользователей

    @GetMapping("/filter")
    public String filterCertificates(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "experts", required = false) List<Long> expertIds,
            Model model) {
        List<User> experts;
        if (expertIds != null) {
            experts = userRepository.findAllById(expertIds);
            List<Certificate> filteredCertificates = certificateService.filterCertificates(startDate, endDate, experts);
            model.addAttribute("certificates", filteredCertificates);
            model.addAttribute("experts", experts);
            return "admin/home/adminHome";
        }
        experts = userRepository.findByRole("USER");
        List<Certificate> filteredCertificates = certificateService.filterCertificates(startDate, endDate, experts);
        model.addAttribute("certificates", filteredCertificates);
        model.addAttribute("experts", experts);
        return "admin/home/adminHome";
    }

}