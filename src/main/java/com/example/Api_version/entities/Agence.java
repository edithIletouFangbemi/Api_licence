package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agence {
    @Id
    private String codeAgence;
    private String nom;
    private String description;
    private String adresse;
    @ManyToOne
    private Institution institution;
    private int statut;
    private LocalDateTime dateCreation;
    private LocalDateTime date_suppression;

}
