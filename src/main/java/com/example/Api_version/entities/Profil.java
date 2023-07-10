package com.example.Api_version.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profil {
    @Id
    private String codeProfil;
    private String libelle;
    @ManyToMany
    private Collection<Habilitation> habilitations;
}
