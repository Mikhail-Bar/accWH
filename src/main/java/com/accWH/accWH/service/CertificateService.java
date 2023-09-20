package com.accWH.accWH.service;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;


    @Autowired
    public CertificateService(CertificateRepository certificateRepository, UserRepository userRepository) {
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
    }

    public List<Certificate> filterCertificates(LocalDate startDate, LocalDate endDate, List<User> experts) {
        if (startDate == null && endDate == null && experts.isEmpty()) {
            return certificateRepository.findAll();
        } else {
            return certificateRepository.findByDateCertificateBetweenAndUserIn(
                    startDate != null ? LocalDate.from(startDate.atStartOfDay()) : LocalDate.parse("2000-01-01"),
                    endDate != null ? LocalDate.from(endDate.atTime(23, 59, 59)) : LocalDate.parse("2999-12-31"),
                    experts
            );
        }
    }
}