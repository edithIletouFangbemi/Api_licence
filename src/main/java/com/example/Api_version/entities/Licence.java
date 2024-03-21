package com.example.Api_version.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Licence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String codeLicence;
    private String libelle;
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    private LocalDateTime dateEffet;
    private LocalDateTime dateExpiration;
    private String keyLicencePoste;
    @ManyToOne
    @JoinColumn(name = "poste_id")
    private Poste poste;
    @ManyToOne
    private Module module;
    private int statut;
    @ManyToOne
    private Parametre parametre;
}
