package com.example.Api_version.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgenceRequest {
    private int instId;
    private String codeAgence;
    private String nom;
    private String description;
    private String adresse;
    private String institutionCode;
}
