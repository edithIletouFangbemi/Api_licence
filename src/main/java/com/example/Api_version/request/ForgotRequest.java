package com.example.Api_version.request;

import lombok.Data;

@Data
public class ForgotRequest {
    private String email;
    private String lastname;
    private String firstname;
}
