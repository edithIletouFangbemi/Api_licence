package com.example.Api_version.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException{
    private HttpStatus typeError;
    public UserException(String message, HttpStatus status){
        super(message);
        this.typeError = status;
    }

    public HttpStatus getTypeErreur() {
        return typeError;
    }
}
