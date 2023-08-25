package com.example.Api_version.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Module {
    @Id
    private String codeModule;
    private String libelleModule;
    @ManyToOne
    private Produit produit;
    private String description;
    @Column(name = "type_module")
    private String typeModule;
    private LocalDateTime dateCreation;
    private LocalDateTime dateSuppression;
    private int statut;

}