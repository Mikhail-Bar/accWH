package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/certificate")
public class AdminCertificateController {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listCertificates(Model model) {
        List<Certificate> certificates = certificateRepository.findAll();
        model.addAttribute("certificates", certificates);
        return "admin/certificate/list";
    }

    @GetMapping("/add")
    public String addCertificateForm(Model model) {
        List<User> users = userRepository.findAll();
        Certificate certificate = new Certificate();
        model.addAttribute("certificate", certificate);
        model.addAttribute("users", users);
        return "admin/certificate/add";
    }

    @PostMapping("/add")
    public String addCertificate(@ModelAttribute Certificate certificate) {
        certificateRepository.save(certificate);
        return "redirect:/admin/certificate";
    }

    @GetMapping("/{certificateId}/edit")
    public String editCertificateForm(@PathVariable Long certificateId, Model model) {
        List<User> users = userRepository.findAll();
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        model.addAttribute("certificate", certificate);
        model.addAttribute("users", users);
        return "admin/certificate/edit";
    }

    @PostMapping("/{certificateId}/edit")
    public String editCertificate(@PathVariable Long certificateId, @ModelAttribute Certificate certificate) {
        Certificate existingCertificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        existingCertificate.setForm(certificate.getForm());
        existingCertificate.setCompleted(certificate.isCompleted());
        existingCertificate.setCompletionDate(certificate.getCompletionDate());
        existingCertificate.setUser(certificate.getUser());

        certificateRepository.save(existingCertificate);
        return "redirect:/admin/certificate";
    }

    @GetMapping("/{certificateId}/delete")
    public String deleteCertificate(@PathVariable Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        certificateRepository.delete(certificate);
        return "redirect:/admin/certificate";
    }

    @GetMapping("/complete/{id}")
    public String completeCertificate(@PathVariable("id") Long id) {
        Optional<Certificate> optionalCertificate = certificateRepository.findById(id);

        if (optionalCertificate.isPresent()) {
            Certificate certificate = optionalCertificate.get();
            if (!certificate.isCompleted()) {
                certificate.setCompleted(true);
                certificate.setCompletionDate(LocalDate.now());
                certificateRepository.save(certificate);
            }
        }

        return "redirect:/";
    }
}
