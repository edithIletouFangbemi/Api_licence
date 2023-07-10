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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Habilitation {
    @Id
    private String codeHabilitation;
    private String libelle;
    private int statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateSuppression;
}
