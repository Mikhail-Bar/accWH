package com.accWH.accWH.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Expert")
public class Expert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @Column (name = "Fname")
    private String Fname;
    @Column (name = "Lname")
    private String Lname;
    @Column (name = "department")
    private String dep;
    @OneToMany(mappedBy = "expert")
    private List<Certificate> certificates;
    @OneToOne(mappedBy = "expert")
    private User username;

}
