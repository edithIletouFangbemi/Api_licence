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
    private String type;
    private int nbrPoste;
    private String module ;
    private String agence;
    private Date dateDebut;
    private Date dateFin;
}
