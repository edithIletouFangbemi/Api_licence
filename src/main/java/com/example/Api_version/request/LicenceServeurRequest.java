package com.example.Api_version.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LicenceServeurRequest {
  private String institutionCode;
  private String agenceCode;
  private String module;
}
