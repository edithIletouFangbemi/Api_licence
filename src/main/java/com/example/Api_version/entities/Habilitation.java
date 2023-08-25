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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Habilitation {
    @Id
    private String codeHabilitation;
    @NotBlank(message = "ce champ est obligatoire, renseignez le svp !")
    private String libelle;
    private int statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateSuppression;
    @JsonIgnoreProperties
    @OneToMany
    private Collection<SousHabilitation> sousHabilitations;
}
