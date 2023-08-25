package com.example.Api_version.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException{
    public UserException(String message, HttpStatus status){
        super(message);
    }
}
