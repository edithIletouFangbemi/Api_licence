package com.example.Api_version.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProfilRequest {
    @NotBlank(message = "le libelle est obligatoire!!")
    private String libelle;
    @NotBlank(message = "vous devez fournir une liste d'habilitations!!")
    private List<String> habilitations;
}
