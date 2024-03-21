package com.example.Api_version.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Poste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String codePoste;
    @NotBlank(message = "Obligatoire!!")
    private String adresseMac;
    @NotBlank(message = "Obligatoire!!")
    private String adresseIp;
    @NotBlank(message = "Obligatoire!!")
    private String idMachine;
    @NotBlank(message = "Obligatoire!!")
    private String idDisque;
    private int statut;
    @ManyToOne
    private Agence agence;
    private LocalDateTime dateCreation;
    private LocalDateTime dateEffet;
    private LocalDateTime dateDesactivation;

}
