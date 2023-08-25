package com.example.Api_version.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contrat_Institution {
    @Id
    private String codeContrat;
    private String libelleContrat;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private int nbrPosteTotal;
    private int nbrAgence;
    private int statut;
    @ManyToOne
    private Institution institution;
    @OneToOne
    private Produit produit;
    private  String typeContrat;
    private String type;

    @PostPersist
    private void onInsert(){
        if (this.codeContrat == null){
            String inc ="000000" + this.codeContrat;
            String usr = "000" + this.produit.getCodeProduit();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            this.codeContrat = this.institution.getCodeInst()+ "CON" + sdf.format(dateFin) + inc.substring(inc.length() - 6);
        }
    }

}
