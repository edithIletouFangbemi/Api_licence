package com.example.Api_version.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametreDeVieRequest {
    private int quantite;
    private String typeParametre;
    private String typeLicence;
}
