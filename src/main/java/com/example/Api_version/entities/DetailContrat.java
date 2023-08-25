package com.example.Api_version.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailContrat {
    @Id
    private String codeDetailContrat;
    private String libelle;
    private Date date_debut;
    private Date date_fin;
    private String typeContratModule;
    private int nbrPoste;
    private int statut;
    @ManyToOne
    private Agence agence;
    @OneToOne
    private Module module;

}
