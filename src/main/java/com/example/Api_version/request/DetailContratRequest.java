package com.example.Api_version.request;

import lombok.Data;

import java.util.List;

@Data
public class DetailContratRequest {
    private int nbrPoste;
    private String agence;
    private List<String> modules;
}
