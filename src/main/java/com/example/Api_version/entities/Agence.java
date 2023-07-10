package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Agence {
    @Id
    private String codeAgence;
    private String nom;
    private String description;
    private String adresse;
    private int statut;
    private LocalDateTime dateCreation;
    private LocalDateTime date_suppression;

}
