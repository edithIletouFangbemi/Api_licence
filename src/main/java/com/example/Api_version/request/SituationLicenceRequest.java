package com.example.Api_version.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SituationLicenceRequest {
    private int idLicence;
    private String idMachine;
    private String libelleAgence;
    private String libelleModule;
    private String typeLicence;
    private int statut;
    private Date dateCreation;
}
