package com.example.Api_version.request;

import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.Module;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DetailContratRequest {

    private String libelle;
    private Date date_debut;
    private Date date_fin;
    private String typeContratModule;
    private int nbrPoste;
    private int statut;
    private int idAgence;
    private int idModule;
    private int idSousContrat;
    private int idContrat;

}


