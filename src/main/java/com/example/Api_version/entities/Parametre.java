package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parametre {
    @Id
    private String codeParametre;
    private String libelle;
    private String description;
    private int statut;
    private Date dateDebut;
    private LocalDateTime dateFin;

}
