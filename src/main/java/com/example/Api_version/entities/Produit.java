package com.example.Api_version.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Produit {
    @Id
    private String codeProduit;
    @NotBlank(message = "Obligatoire!!")
    private String nom;
    private String description;
    private int statut;
    private LocalDateTime dateCreation;
    private LocalDateTime datSuppression;

}
