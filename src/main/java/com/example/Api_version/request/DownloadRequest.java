package com.example.Api_version.request;

import lombok.Data;

@Data
public class DownloadRequest {
    private String codelicence;
    private String key;
    private String codeAgence;
    private String codemodule;
    private String codeinst;
}
