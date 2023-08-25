package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DetailContratModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String codeAgence;
    private String codeDetail;
    private String codeModule;
    private LocalDateTime dateCreation;
    private LocalDateTime dateSuppression;
    private int statut;
}
