package com.example.Api_version.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contrat_Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String codeContrat;
    private String libelleContrat;
    private Date dateDebut;
    private Date dateFin;
    private int nbrPosteTotal;
    private int nbrAgence;
    private int statut;
    private String typeContrat;
    private Date dateCreation;
    private Date dateSuppression;
    @ManyToOne
    private Institution institution;
    @OneToMany
    private List<Sous_Contrat> listSousContrat = new ArrayList<>();

    @PostPersist
    private void onInsert(){
        if (this.codeContrat == null){
            String inc ="000000" + this.codeContrat;
            String usr = "000" + this.institution.getCodeInst();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            this.codeContrat = this.institution.getCodeInst()+ "CON" + sdf.format(dateFin) + inc.substring(inc.length() - 6);
        }
    }

}
