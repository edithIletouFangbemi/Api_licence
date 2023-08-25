package com.example.Api_version.request;

import lombok.Data;

@Data
public class ModuleDetail {
    private String codeModule;
    private String libelleModule;
    private int nbrLicenceEnAttente;
    private int nbrLicenceActive;
    private int nbrLicenceInactive;
}
