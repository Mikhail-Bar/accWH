package com.accWH.accWH.service;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import com.accWH.accWH.repository.CertificateRepository;
import com.accWH.accWH.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;

@Service
public class ExcelImportService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ExcelImportService.class);

    public void importDataFromExcel(MultipartFile multipartFile) throws IOException {
        try {
        File file = convertMultiPartToFile(multipartFile);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) {
                continue;
            }

            Certificate certificate = new Certificate();
            certificate.setForm(getStringValue(row.getCell(0)));
            certificate.setCompleted(getBooleanValue(row.getCell(1)));
            certificate.setDateCertificate(getLocalDateValue(row.getCell(2)));
            certificate.setCompletionDate(getLocalDateValue(row.getCell(3)));
            certificate.setBlankNumber(getStringValue(row.getCell(6)));
            String expertName = getStringValue(row.getCell(4));
            String[] expertNameParts = expertName.split("\\.");

            String lastNameWithMaxChars = "";
            for (String namePart : expertNameParts) {
                if (namePart.length() > lastNameWithMaxChars.length()) {
                    lastNameWithMaxChars = namePart;
                }
            }
            User user = userRepository.findBylname(lastNameWithMaxChars);
            if (user != null) {
                certificate.setUser(user);
            }
            String certificateNumber = getStringValue(row.getCell(5));

            Certificate existingCertificate = certificateRepository.findByCertificateNumber(certificateNumber);
            if (existingCertificate != null) {
                existingCertificate.setForm(certificate.getForm());
                existingCertificate.setCompleted(certificate.isCompleted());
                existingCertificate.setDateCertificate(certificate.getDateCertificate());
                existingCertificate.setCompletionDate(certificate.getCompletionDate());
                existingCertificate.setBlankNumber(certificate.getBlankNumber());
                existingCertificate.setUser(certificate.getUser());

                certificateRepository.save(existingCertificate);
            } else {
                certificate.setCertificateNumber(certificateNumber);
                certificateRepository.save(certificate);
            }

        }

        workbook.close();
        inputStream.close();
        } catch (Exception e) {
            // Логируйте ошибку
            logger.error("An error occurred during data import:", e);
        }
    }

    private String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    private boolean getBooleanValue(Cell cell) {
        if (cell == null) {
            return false;
        }
        cell.setCellType(CellType.BOOLEAN);
        return cell.getBooleanCellValue();
    }

    private LocalDate getLocalDateValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return LocalDate.parse(cell.getStringCellValue());
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}

