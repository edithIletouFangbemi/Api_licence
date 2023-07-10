package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Poste {
    @Id
    private String codePoste;
    private String adresseMac;
    private String adresseIp;
    private String idMachine;
    private String idDisque;
    private int statut;
}
