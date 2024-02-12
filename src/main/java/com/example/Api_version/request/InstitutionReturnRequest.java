package com.example.Api_version.request;

import lombok.Data;

import java.util.Date;

@Data
public class InstitutionReturnRequest {
    private int id;
    private String codeInst;
    private String nomInst;
    private String adresseInst;
    private String typeArchitecture;
    private Date dateCreation;
    private String hasContrat;
    private int statut;
    private int nbrAgence;
}
