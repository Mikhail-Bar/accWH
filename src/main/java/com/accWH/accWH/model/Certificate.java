package com.accWH.accWH.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Certificate", uniqueConstraints = {@UniqueConstraint(columnNames = {"certificateNumber", "blankNumber"})})
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "Form")
    private String form;

    @Column(name = "Completed")
    private boolean completed;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "DateCertificate")
    private LocalDate dateCertificate;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "CompletionDate")
    private LocalDate completionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "certificateNumber", unique = true)
    private String certificateNumber;

    @Column(name = "blankNumber", unique = true)
    private String blankNumber;
}
