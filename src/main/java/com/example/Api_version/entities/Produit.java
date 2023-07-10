package com.example.Api_version.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Produit {
    @Id
    private String codeProduit;
    private String nom;
    private String description;
    @OneToMany
    private Collection<Module> modules;


}
