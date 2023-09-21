package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.UserRepository;
import com.accWH.accWH.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/certificates")
public class AdminCertificateController {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CertificateService certificateService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listCertificates(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
        if (userOptional.isPresent() && userOptional.get().isAdmin()) {
            List<User> experts = userRepository.findByRole("USER");
            List<Certificate> certificates = certificateService.filterCertificates(LocalDate.parse("2000-01-01"), LocalDate.parse("2999-12-31"), experts);
            Map<String, Long> certificateCounts = certificateService.countTotalCompletedAndNotCompletedCertificates(LocalDate.parse("2000-01-01"), LocalDate.parse("2999-12-31"), experts);
            model.addAttribute("certificateCounts", certificateCounts);
            model.addAttribute("certificates", certificates);
            model.addAttribute("experts", experts);
            return "admin/home/adminHome";
        } else {
            throw new IllegalArgumentException("Вы не администратор: " + username);
        }
    }
    @GetMapping("/{id}/edit")
    public String editCertificateForm(@PathVariable Long id, Model model) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + id));

        model.addAttribute("certificate", certificate);
        return "admin/certificate/edit";
    }

    @PostMapping("/{id}/edit")
    public String editCertificate(@PathVariable Long id, @ModelAttribute Certificate certificate) {
        Certificate existingCertificate = certificateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + id));

        existingCertificate.setForm(certificate.getForm());
        existingCertificate.setCompleted(certificate.isCompleted());
        if (!certificate.isCompleted()) {
            existingCertificate.setCompletionDate(null);
        } else {
            existingCertificate.setCompletionDate(certificate.getCompletionDate());
        }

        certificateRepository.save(existingCertificate);
        return "redirect:/";
    }

    @GetMapping("/{certificateId}/delete")
    public String deleteCertificate(@PathVariable Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        certificateRepository.delete(certificate);
        return "redirect:/admin/certificates";
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
