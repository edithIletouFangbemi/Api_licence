package com.example.Api_version.request;

import com.example.Api_version.entities.Profil;
import com.example.Api_version.entities.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReturnRequest {
    private String codeUser;
    private String lastname;
    private String firstname;
    private String email;
    private Profil profil;
    private int statut;
    private  String token;

}
