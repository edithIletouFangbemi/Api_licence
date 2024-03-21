package com.example.Api_version.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LicenceServeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private String libelle;
    private String keyLicenceServeur;
    private int statut;
    @ManyToOne
    private Agence agence;
    @ManyToOne
    @JoinColumn(name = "poste_id")
    private Poste poste;
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    private LocalDateTime dateDesactivation;
    @ManyToOne
    private Module module;
    @ManyToOne
    private Parametre parametre;
}
