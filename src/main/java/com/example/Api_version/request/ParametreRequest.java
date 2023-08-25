package com.example.Api_version.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class ParametreRequest {
    @NotBlank(message = "la liste des parametres ne peut pas etre vide!!")
    private List<String> parametres;
    @NotBlank(message = "Fournir la date de debut")
    private Date dateDebut;

}
