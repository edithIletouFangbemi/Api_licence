package com.example.Api_version.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "email obligatoire")
    private String email;
    @NotBlank(message = "mot de passe obligatoire!!")
    private String password;
}
