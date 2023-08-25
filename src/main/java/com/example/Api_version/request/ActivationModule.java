package com.example.Api_version.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ActivationModule {
    private String codeModule;
    private String type;
    private int nbrPoste;
    private Date dateDebut;
    private Date dateFin;
}
