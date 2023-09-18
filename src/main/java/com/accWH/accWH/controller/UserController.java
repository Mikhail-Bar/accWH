package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/certificate")
public class UserController {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listCertificates(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Certificate> certificates = user.getCertificates();

            model.addAttribute("certificates", certificates);
        } else {
            throw new IllegalArgumentException("Пользователь не найден: " + username);
        }

        return "user/certificate/list";
    }

    @GetMapping("/add")
    public String addCertificateForm(Model model) {
        Certificate certificate = new Certificate();
        model.addAttribute("certificate", certificate);
        return "user/certificate/add";
    }

    @PostMapping("/add")
    public String addCertificate(@ModelAttribute Certificate certificate, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            certificate.setUser(user);
            certificateRepository.save(certificate);
            return "redirect:/user/certificate";
        } else {
            return "redirect:/user/certificates?error=user_not_found";
        }
    }

    @GetMapping("/{certificateId}/edit")
    public String editCertificateForm(@PathVariable Long certificateId, Model model) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        model.addAttribute("certificate", certificate);
        return "user/certificate/edit";
    }

    @PostMapping("/{certificateId}/edit")
    public String editCertificate(@PathVariable Long certificateId, @ModelAttribute Certificate certificate) {
        Certificate existingCertificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        existingCertificate.setForm(certificate.getForm());
        existingCertificate.setCompleted(certificate.isCompleted());
        existingCertificate.setCompletionDate(certificate.getCompletionDate());

        certificateRepository.save(existingCertificate);
        return "redirect:/user/certificate";
    }

    @GetMapping("/{certificateId}/delete")
    public String deleteCertificate(@PathVariable Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        certificateRepository.delete(certificate);
        return "redirect:/user/certificate";
    }
}
