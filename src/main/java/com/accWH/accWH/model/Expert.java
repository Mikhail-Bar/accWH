package com.accWH.accWH.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Expert")
public class Expert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "id", nullable = false)
    private long id;
    @Column (name = "Fname")
    private String Fname;
    @Column (name = "Lname")
    private String Lname;
    @Column (name = "department")
    private String dep;
}
