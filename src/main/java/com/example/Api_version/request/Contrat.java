package com.example.Api_version.request;

import com.example.Api_version.entities.Institution;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class Contrat implements Serializable {
    private String institution;
    private String produit;
    private String typeContrat;
    private int nbrAgence;
    private Date dateDebut;
    private Date dateFin;
    private List<ContratUnit> contratUnits = new LinkedList<>();

}
