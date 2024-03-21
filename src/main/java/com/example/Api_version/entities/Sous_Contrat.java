package com.example.Api_version.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sous_Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Produit produit;
    @OneToMany
    private List<DetailContrat> listDetailsContrat = new ArrayList<>();
    private int statut;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Temporal(TemporalType.DATE)
    private Date dateFin;

    private int nbrPosteTotal;
    private int nbrAgence;

    private LocalDateTime dateCreation;
    private LocalDateTime dateSuppression;

    private  String typeContrat;
    @ManyToOne
    @JsonIgnoreProperties
    private Contrat_Institution contrat;
    // 0 : contrat ; 1 : avenant
    private int type;
}
