package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.UserRepository;
import com.accWH.accWH.repository.specifications.CertificateSpecification;
import com.accWH.accWH.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    public String listCertificates(Model model, Authentication authentication,
                                   @RequestParam(value = "startDate", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(value = "endDate", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                   @RequestParam(value = "expertIds", required = false) List<Long> expertIds) {
        String username = authentication.getName();
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
        if (userOptional.isPresent() && userOptional.get().isAdmin()) {

            List<User> selectedExperts = new ArrayList<>();
            if (expertIds != null) {
                for (Long expertId : expertIds) {
                    Optional<User> expertOptional = userRepository.findById(expertId);
                    expertOptional.ifPresent(selectedExperts::add);
                }
            }
            Specification<Certificate> spec = CertificateSpecification.dateCertificateBetweenAndUserIn(startDate, endDate, selectedExperts);
            List<Certificate> certificates = certificateRepository.findAll(spec);
            List<User> experts = userRepository.findByRole("USER");
            model.addAttribute("certificates", certificates);
            model.addAttribute("experts", experts);

            return "admin/home/adminHome";
        } else {
            throw new IllegalArgumentException("Вы не администратор: " + username);
        }
    }
    @GetMapping("/{certificateId}/edit")
    public String editCertificateForm(@PathVariable Long certificateId, Model model) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        model.addAttribute("certificate", certificate);
        return "admin/editCertificate";
    }

    @PostMapping("/{certificateId}/edit")
    public String editCertificate(@PathVariable Long certificateId, @ModelAttribute Certificate certificate) {
        Certificate existingCertificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        existingCertificate.setForm(certificate.getForm());
        existingCertificate.setCompleted(certificate.isCompleted());
        existingCertificate.setCompletionDate(certificate.getCompletionDate());

        certificateRepository.save(existingCertificate);
        return "redirect:/admin/certificates";
    }

    @GetMapping("/{certificateId}/delete")
    public String deleteCertificate(@PathVariable Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        certificateRepository.delete(certificate);
        return "redirect:/admin/certificates";
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            return LocalDate.parse(dateStr);
        }
        return null;
    }
}
