package com.example.Api_version.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
