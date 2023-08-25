package com.example.Api_version.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SousHabilitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String libelle;
    @ManyToOne
    private Habilitation habilitation;
    private int statut;
}
