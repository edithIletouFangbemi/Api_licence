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
public class Contrat_Institution {
    @Id
    private String codeContrat;
    private String libelleContrat;
    private LocalDateTime date_debut;
    private  LocalDateTime date_fin;
    private int nbrPosteTotal;
    private int nbrAgence;

    private int statut;

}
