package com.accWH.accWH.repository;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {
    List<Certificate> findByDateCertificateBetweenAndUserIn(LocalDate startDate, LocalDate endDate, List<User> experts);


    long countByDateCertificateBetweenAndUserInAndCompleted(LocalDate startDate, LocalDate endDate, List<User> experts, boolean b);


    List<Certificate> findByDateCertificateBetweenAndUser(LocalDate startDate, LocalDate endDate, User expert);

    Certificate findByCertificateNumber(String certificateNumber);

    Page<Certificate> findCertificatesByDateCertificateBetweenAndUserIn(LocalDate startDate, LocalDate endDate, List<User> experts, Pageable pageable);

}