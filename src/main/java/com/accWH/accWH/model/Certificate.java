package com.accWH.accWH.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @Column (name = "Form")
    private String form;
    @Column (name = "Completed")
    private boolean completed;
    @Column(name = "DateCertificate")
    private LocalDate dateCertificate;
    @Column(name = "CompletionDate")
    private LocalDate completionDate;
    @ManyToOne
    @JoinColumn(name = "expert_id")
    private Expert expert;
}
