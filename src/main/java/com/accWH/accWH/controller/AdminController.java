package com.accWH.accWH.controller;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.UserRepository;
import com.accWH.accWH.service.CertificateService;
import com.accWH.accWH.service.ExcelImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CertificateService certificateService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/filter1")
    public String filterCertificates(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate,
            @RequestParam(value = "experts", required = false) List<Long> expertIds,
            @RequestParam(value = "page", defaultValue = "0") int page, // Страница по умолчанию
            @RequestParam(value = "size", defaultValue = "10") int size, // Размер страницы по умолчанию
            Model model) {
        Pageable pageable = PageRequest.of(page, size);

        List<User> experts;
        if (expertIds != null) {
            experts = userRepository.findAllById(expertIds);
        } else {
            experts = userRepository.findByRole("USER");
        }

        Page<Certificate> certificates = certificateService.filterCertificatesPageable(startDate, endDate, experts, pageable);

        Map<String, Long> certificateCounts = certificateService.countTotalCompletedAndNotCompletedCertificates(startDate, endDate, experts);
        model.addAttribute("certificateCounts", certificateCounts);
        model.addAttribute("certificates", certificates.getContent()); // Получить содержимое текущей страницы
        model.addAttribute("experts", experts);

        return "admin/home/adminHome";
    }

    @GetMapping("/expertCertificateCounts")
    public String expertCertificateCounts(Model model) {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<User> experts = userRepository.findByRole("USER");

        Map<User, Map<String, Map<String, Long>>> expertMonthCertificateCounts = certificateService.countCompletedAndNotCompletedCertificatesByExpertAndForm(startDate, endDate, experts);

        model.addAttribute("expertMonthCertificateCounts", expertMonthCertificateCounts);
        return "admin/home/expertCertificateCounts";
    }
    @Autowired
    private ExcelImportService excelImportService;

    @GetMapping("/upload")
    public String showUploadForm() {
        return "admin/home/uploadForm";
    }

    @PostMapping("/import")
    public String importExcelData(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                excelImportService.importDataFromExcel(file);
                return "redirect:/admin/certificates";
            } catch (Exception e) {
                return "redirect:/error";
            }
        } else {
            return "redirect:/error";
        }
    }
}