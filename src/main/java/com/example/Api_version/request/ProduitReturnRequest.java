package com.example.Api_version.request;

import lombok.Data;

@Data
public class ProduitReturnRequest {
    private String codeProduit;
    private String nom;
    private String description;
    private int nbrModuleStandard;
    private int nbrModuleAdditionnel;
    private int statut;
}
