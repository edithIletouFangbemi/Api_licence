package com.example.Api_version.request;

import com.example.Api_version.entities.Produit;
import lombok.Data;

import java.util.Collection;

@Data
public class AgenceDetail {
    private String codecontrat;
    private String typeContrat;
    private int nbrAgence;
    private int nbrPosteTotal;
    private int nbrPosteActif;
    private Collection<ProduitDetail> produits;
}
