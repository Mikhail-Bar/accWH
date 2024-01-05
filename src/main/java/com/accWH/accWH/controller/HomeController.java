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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
public class HomeController {
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CertificateService certificateService;

    @GetMapping("/")
    public String home(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            Model model
    ) {
        if (authentication != null) {
            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                List<User> experts = userRepository.findByRole("USER");

                // Создайте объект Pageable для запроса страницы
                Pageable pageable = PageRequest.of(page - 1, 10); // 10 - размер страницы

                // Используйте pageable для запроса страницы сертификатов
                Page<Certificate> certificates = certificateService.filterCertificatesPageable(
                        LocalDate.parse("2000-01-01"),
                        LocalDate.parse("2999-12-31"),
                        experts,
                        pageable
                );

                Map<String, Long> certificateCounts = certificateService.countTotalCompletedAndNotCompletedCertificates(
                        LocalDate.parse("2000-01-01"),
                        LocalDate.parse("2999-12-31"),
                        experts
                );

                model.addAttribute("certificateCounts", certificateCounts);
                model.addAttribute("certificates", certificates.getContent()); // Получите список сертификатов из Page
                model.addAttribute("experts", experts);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", certificates.getTotalPages());

                return "admin/home/adminHome";
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    List<Certificate> certificates = user.getCertificates();
                    model.addAttribute("user", user);
                    model.addAttribute("certificates", certificates);
                } else {
                    model.addAttribute("user", null);
                    model.addAttribute("certificates", Collections.emptyList());
                }
                return "user/home/expertHome";
            }
        }
        return "redirect:/";
    }
}
