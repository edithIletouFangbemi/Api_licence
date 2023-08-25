package com.example.Api_version.request;

import com.example.Api_version.entities.Institution;
import com.example.Api_version.entities.Produit;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

@Data
public class DetailContratInstitutionRequest {
    private String codeContrat;
    private String libelleContrat;
    private Date dateDebut;
    private Date dateFin;
    private int nbrPosteTotal;
    private int nbrAgence;
    private int statut;
    private String typeContrat;
    private Institution  institution;
    private Collection<Produit> produits;


}
