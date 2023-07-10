package com.example.Api_version.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Licence {
    @Id
    private String codeLicence;
    private String libelle;
    private LocalDateTime date_création;
    private LocalDateTime date_expiration;
    private TypeLicence typeLicence;
    private int statut;
    @ManyToOne
    private Parametre parametre;
}
