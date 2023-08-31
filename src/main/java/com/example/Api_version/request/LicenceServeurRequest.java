package com.example.Api_version.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LicenceServeurRequest {
  private String institutionCode;
  private String agenceCode;
  private String produit;
  private List<String> modules;
}
