package com.example.Api_version.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HabilitationRequest {
    @NotBlank(message = "le libelle de l'habilitation est obligatoire!!")
    private String libelle;

}
