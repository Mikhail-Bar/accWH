package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.UserRepository;
import com.accWH.accWH.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public String listCertificates(
            @RequestParam(defaultValue = "1") int page, // Страница по умолчанию - 1
            @RequestParam(defaultValue = "10") int size, // Размер страницы по умолчанию - 10
            Model model, Authentication authentication
    ) {
        String username = authentication.getName();
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        if (userOptional.isPresent() && userOptional.get().isAdmin()) {
            List<User> experts = userRepository.findByRole("USER");

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by("dateCertificate").descending());

            Page<Certificate> certificatePage = certificateService.filterCertificatesPageable(
                    LocalDate.parse("01.01.2000", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    LocalDate.parse("31.12.2999", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    experts, pageable
            );

            Map<String, Long> certificateCounts = certificateService.countTotalCompletedAndNotCompletedCertificates(
                    LocalDate.parse("01.01.2000", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    LocalDate.parse("31.12.2999", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    experts
            );

            model.addAttribute("certificateCounts", certificateCounts);
            model.addAttribute("certificates", certificatePage.getContent());
            model.addAttribute("experts", experts);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", certificatePage.getTotalPages());

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
