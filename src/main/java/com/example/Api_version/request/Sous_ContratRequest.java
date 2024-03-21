package com.example.Api_version.request;

import com.example.Api_version.entities.DetailContrat;
import com.example.Api_version.entities.Produit;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Sous_ContratRequest {
    private int idProduit;
    private Date dateDebut;
    private Date dateFin;
    private int nbrPosteTotal;
    private int nbrAgence;
    private String typeContrat;
    private int idContrat;
    private List<DetailContratRequest> detailContratRequests;
}
