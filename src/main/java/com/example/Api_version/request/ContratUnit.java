package com.example.Api_version.request;

import com.example.Api_version.entities.Produit;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class ContratUnit implements Serializable {
    private String produit;
    private int nbrPoste;
    private int nbrAgence;
    private List<String> modules = new LinkedList<>();
    private List<String> agences = new LinkedList<>();
    private String typeContrat;
    private Date dateDebut;
    private Date dateFin;
    private String codeContrat;
}
