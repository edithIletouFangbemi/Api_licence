package com.example.Api_version.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoContrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String codeContrat;
    private String libelleContrat;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private int nbrPosteTotal;
    private int nbrAgence;
    private int statut;
    @ManyToOne
    private Institution institution;
    @OneToOne
    private Produit produit;
    private String type;
    private  String typeContrat;
}
