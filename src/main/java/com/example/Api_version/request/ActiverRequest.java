package com.example.Api_version.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ActiverRequest {
    @NotBlank(message = "champ obligatoire!!")
    private String codeAgence;

    @NotBlank(message = "champ obligatoire!!")
    private String codeProduit;

    @NotBlank(message = "champ obligatoire!")
    private List<String> modules;

    private int nbrPoste;
}
