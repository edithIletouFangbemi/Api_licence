package com.example.Api_version.request;

import lombok.Data;

@Data
public class LicenceRecapRequest {
    private String codeInst;
    private String nomInst;
    private String typeArchitecture;
    private int nbrProduitActif;
}
