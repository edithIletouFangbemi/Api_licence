package com.example.Api_version.request;

import lombok.Data;

@Data
public class ModuleRequest {
    private String codeModule;
    private String libelleModule;
    private String description;
    private String typeModule;
    private int produitId;
}
