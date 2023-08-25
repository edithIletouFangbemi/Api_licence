package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contratInstitution;
    private int statut;
    private Date dateCreation;
    private Date dateFin;
    private String produit;
    private int nbrPoste;
    private int nbrAgence;
    private String typeContrat;

}
