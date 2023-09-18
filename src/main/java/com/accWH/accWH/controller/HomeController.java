package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.Expert;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.ExpertRepository;
import com.accWH.accWH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private ExpertRepository expertRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication != null) {
            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "admin/home/adminHome";
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    Expert expert = user.getExpert();
                    model.addAttribute("expert", expert);
                    List<Certificate> certificates = expert.getCertificates();
                    model.addAttribute("certificates", certificates);
                } else {
                    model.addAttribute("expert", null);
                    model.addAttribute("certificates", Collections.emptyList());
                }
                return "user/home/expertHome";
            }
        }
        return "redirect:/";
    }
}
