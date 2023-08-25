package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Licence {
    @Id
    private String codeLicence;
    private String libelle;
    private LocalDateTime dateCreation;
    private LocalDateTime dateEffet;
    private LocalDateTime dateExpiration;
    private String key;
    @ManyToOne
    private Poste poste;
    @ManyToOne
    private Module module;
    private int statut;
}
