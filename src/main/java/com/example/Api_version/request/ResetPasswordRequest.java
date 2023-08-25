package com.example.Api_version.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String verificationToken;
    private String email;
    private String lastname;
    private String firstname;
    private String oldPassword;
    private String newPassword;
}
