package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenceModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String codeLicence;
    private String codeModule;
}
