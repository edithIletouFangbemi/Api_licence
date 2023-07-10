package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailContrat {
    @Id
    private String codeDetailContrat;
    private String libelle;
    private LocalDateTime date_debut;
    private LocalDateTime date_fin;
    private int nbPoste;
}
