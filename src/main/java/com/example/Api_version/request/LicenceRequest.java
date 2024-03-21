package com.example.Api_version.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LicenceRequest {
    private int agence;
    private int produit;
    private int institution;
    private List<Integer> modules ;
    private String adresseIp;
    private String adresseMac;
    private String idMachine;
    private String idDisqueDur;
}
