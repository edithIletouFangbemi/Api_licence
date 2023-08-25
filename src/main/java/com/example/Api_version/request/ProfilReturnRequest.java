package com.example.Api_version.request;

import lombok.Data;

@Data
public class ProfilReturnRequest {
    private String codeProfil;
    private String libelle;
    private int statut;
    private int nbrHabilitation;

}
