package com.example.Api_version.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ContratRequest {
    @NotBlank(message = "ce champ est obligatoire!")
    private List<String> produits;
    @NotBlank(message = "ce champ est obligatoire!")
    private String institution;

    private int nbrPosteTotal;

    private Date date_debut;
    private Date date_fin;

    private int nbrAgence;
    @NotBlank(message = "ce champ est obligatoire!")
    private String typeContrat;

}
