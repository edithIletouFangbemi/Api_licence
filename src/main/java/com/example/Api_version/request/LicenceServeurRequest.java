package com.example.Api_version.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LicenceServeurRequest {
  private Integer institutionCode;
  private Integer  agenceCode;
  private Integer produit;
  private List<Integer> modules;
  private String adresseIp;
  private String adresseMac;
  private String idMachine;
  private String idDisqueDur;
}
