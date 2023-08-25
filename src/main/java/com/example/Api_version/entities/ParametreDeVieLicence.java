package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ParametreDeVieLicence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantite;
    private String typeParametre;
    private String typeLicence;
    private int statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateFin;
}
