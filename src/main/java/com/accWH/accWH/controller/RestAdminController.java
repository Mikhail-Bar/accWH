package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.FilterResult;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.UserRepository;
import com.accWH.accWH.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class RestAdminController {
    @Autowired
    private CertificateService certificateService;

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/admin/filter")
    public ResponseEntity<?> filterCertificates(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate,
            @RequestParam(value = "experts", required = false) List<Long> expertIds,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        List<User> experts;
        if (expertIds != null) {
            experts = userRepository.findAllById(expertIds);
        } else {
            experts = userRepository.findByRole("USER");
        }

        Page<Certificate> certificates = certificateService.filterCertificatesPageable(startDate, endDate, experts, pageable);

        Map<String, Long> certificateCounts = certificateService.countTotalCompletedAndNotCompletedCertificates(startDate, endDate, experts);

        // Создайте объект, который будет содержать результат фильтрации
        FilterResult filterResult = new FilterResult();
        filterResult.setCertificates(certificates.getContent());
        filterResult.setCertificateCounts(certificateCounts);

        return ResponseEntity.ok(filterResult);
    }
}
