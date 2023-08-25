package com.example.Api_version.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosteDetailRequest {
    private String codeProduit;
    private String nomProduit;
    private String codemodule;
    private String libelleModule;
    private String codePoste;
    private String libellePoste;
    private String codeLicence;
    private int licenceStatut;
}
