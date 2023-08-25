package com.example.Api_version.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgenceRecapRequest {
    private String codeAgence;
    private String nom;
    private int nbrProduitActif;
    private int nbrModuleActif;
}
