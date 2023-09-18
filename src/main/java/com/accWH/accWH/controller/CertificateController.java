package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.Expert;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/certificates")
public class CertificateController {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @GetMapping
    public String listCertificates(Model model) {
        List<Certificate> certificates = certificateRepository.findAll();
        model.addAttribute("certificates", certificates);
        return "certificate/list";
    }

    @GetMapping("/add")
    public String addCertificateForm(Model model) {
        List<Expert> experts = expertRepository.findAll();
        Certificate certificate = new Certificate();
        model.addAttribute("certificate", certificate);
        model.addAttribute("experts", experts);
        return "certificate/add";
    }

    @PostMapping("/add")
    public String addCertificate(@ModelAttribute Certificate certificate) {
        certificateRepository.save(certificate);
        return "redirect:/certificates";
    }

    @GetMapping("/{certificateId}/edit")
    public String editCertificateForm(@PathVariable Long certificateId, Model model) {
        List<Expert> experts = expertRepository.findAll();
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        model.addAttribute("certificate", certificate);
        model.addAttribute("experts", experts);
        return "certificate/edit";
    }

    @PostMapping("/{certificateId}/edit")
    public String editCertificate(@PathVariable Long certificateId, @ModelAttribute Certificate certificate) {
        Certificate existingCertificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        existingCertificate.setForm(certificate.getForm());
        existingCertificate.setCompleted(certificate.isCompleted());
        existingCertificate.setCompletionDate(certificate.getCompletionDate());
        existingCertificate.setExpert(certificate.getExpert());

        certificateRepository.save(existingCertificate);
        return "redirect:/certificates";
    }

    @GetMapping("/{certificateId}/delete")
    public String deleteCertificate(@PathVariable Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сертификата:" + certificateId));

        certificateRepository.delete(certificate);
        return "redirect:/certificates";
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
    @GetMapping("/{id}")
    public String getExpert(@PathVariable("id") Long id, Model model) {
        Optional<Expert> optionalExpert = expertRepository.findById(id);

        if (optionalExpert.isPresent()) {
            Expert expert = optionalExpert.get();
            model.addAttribute("expert", expert);
        } else {
            model.addAttribute("expert", null);
        }

        return "expert/expertHome";
    }
}
