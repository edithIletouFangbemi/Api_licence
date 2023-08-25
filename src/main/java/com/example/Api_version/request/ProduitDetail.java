package com.example.Api_version.request;

import com.example.Api_version.entities.Module;
import lombok.Data;

import java.util.Collection;

@Data
public class ProduitDetail {
    private String codeProduit;
    private String libelle;
    private Collection<ModuleDetail> modules;
    private int nbrPosteProduit;
}
