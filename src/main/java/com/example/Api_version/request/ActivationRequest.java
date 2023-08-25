package com.example.Api_version.request;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ActivationRequest {
    private String codeAgence;
    private String codeInst;
    private String codeProduit;
    private List<ActivationModule> modules;
}
