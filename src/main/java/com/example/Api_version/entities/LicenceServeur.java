package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LicenceServeur {
    @Id
    private String code;
    private String libelle;
    private String key;
    private int statut;
    @OneToOne
    private Agence agence;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDesactivation;
}
