package com.example.Api_version.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LicenceRequest {
    private String agence;
    private String produit;
    private String institution;
    private List<String> modules ;
    private String adresseIp;
    private String adresseMac;
    private String idMachine;
    private String idDisqueDur;
}
