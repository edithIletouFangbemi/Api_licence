package com.example.Api_version.request;

import com.example.Api_version.entities.Institution;
import com.example.Api_version.entities.Sous_Contrat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class Contrat implements Serializable {

    private int institution;
    private String typeContrat;
    private int nbrAgence;
    private Date dateDebut;
    private Date dateFin;
    private String codeContrat;
    private String libelleContrat;
    private int nbrPosteTotal;
    private int statut;
    private List<Sous_ContratRequest> listSousContrat;

}
