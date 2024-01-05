package com.accWH.accWH.service;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CertificateService {
    @Autowired
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
                    startDate != null ? LocalDate.from(startDate.atStartOfDay()) : LocalDate.parse("01.01.2000", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    endDate != null ? LocalDate.from(endDate.atTime(23, 59, 59)) : LocalDate.parse("31.12.2999", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    experts
            );
        }
    }
    public Page<Certificate> filterCertificatesPageable(LocalDate startDate, LocalDate endDate, List<User> experts, Pageable pageable) {
        if (startDate == null && endDate == null && (experts == null || experts.isEmpty())) {
            return certificateRepository.findAll(pageable);
        } else {
            return certificateRepository.findCertificatesByDateCertificateBetweenAndUserIn(
                    startDate != null ? LocalDate.from(startDate.atStartOfDay()) : LocalDate.parse("01.01.2000", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    endDate != null ? LocalDate.from(endDate.atTime(23, 59, 59)) : LocalDate.parse("31.12.2999", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    experts,
                    pageable
            );
        }
    }

    public Map<String, Long> countTotalCompletedAndNotCompletedCertificates(
            LocalDate startDate, LocalDate endDate, List<User> experts) {

        if (startDate == null) {
            startDate = LocalDate.of(2000, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.of(2999, 12, 31);
        }

        long totalCompleted = certificateRepository.countByDateCertificateBetweenAndUserInAndCompleted(
                startDate, endDate, experts, true);

        long totalNotCompleted = certificateRepository.countByDateCertificateBetweenAndUserInAndCompleted(
                startDate, endDate, experts, false);

        Map<String, Long> result = new HashMap<>();
        result.put("completed", totalCompleted);
        result.put("notCompleted", totalNotCompleted);


        return result;
    }

    public Map<User, Map<String, Map<String, Long>>> countCompletedAndNotCompletedCertificatesByExpertAndForm(
            LocalDate startDate, LocalDate endDate, List<User> experts) {
        Map<User, Map<String, Map<String, Long>>> result = new HashMap<>();

        for (User expert : experts) {
            Map<String, Map<String, Long>> expertCounts = new HashMap<>();
            List<Certificate> certificates = certificateRepository.findByDateCertificateBetweenAndUser(startDate, endDate, expert);

            for (Certificate certificate : certificates) {
                String form = certificate.getForm();
                boolean completed = certificate.isCompleted();

                if (!expertCounts.containsKey(form)) {
                    expertCounts.put(form, new HashMap<>());
                }

                Map<String, Long> counts = expertCounts.get(form);
                counts.put(completed ? "completed" : "notCompleted", counts.getOrDefault(completed ? "completed" : "notCompleted", 0L) + 1);
            }

            result.put(expert, expertCounts);
        }

        return result;
    }
}