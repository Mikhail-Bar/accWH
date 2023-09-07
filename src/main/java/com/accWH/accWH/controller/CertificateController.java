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

@Controller
@RequestMapping("/experts/{expertId}/certificates")
public class CertificateController {
    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @GetMapping
    public String listCertificates(@PathVariable Long expertId, Model model) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expert Id:" + expertId));

        List<Certificate> certificates = certificateRepository.findByExpertId(expertId);
        model.addAttribute("certificates", certificates);
        model.addAttribute("expert", expert);
        return "certificate/list";
    }

    @GetMapping("/add")
    public String addCertificateForm(@PathVariable Long expertId, Model model) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expert Id:" + expertId));

        Certificate certificate = new Certificate();
        model.addAttribute("certificate", certificate);
        model.addAttribute("expert", expert);
        return "certificate/add";
    }

    @PostMapping("/add")
    public String addCertificate(@PathVariable Long expertId, @ModelAttribute Certificate certificate) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid expert Id:" + expertId));

        certificate.setExpert(expert);
        certificateRepository.save(certificate);
        return "redirect:/experts/{expertId}/certificates";
    }

    @GetMapping("/{certificateId}/complete")
    public String completeCertificate(@PathVariable Long expertId, @PathVariable Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid certificate Id:" + certificateId));

        certificate.setCompleted(true);
        certificate.setCompletionDate(LocalDate.now());
        certificateRepository.save(certificate);
        return "redirect:/experts/{expertId}/certificates";
    }
}
