package com.example.Api_version.exceptions;

import org.springframework.http.HttpStatus;

public class InstitutionException extends RuntimeException{
    private HttpStatus typeError;
    public InstitutionException(String message, HttpStatus typeError){
        super(message);
        this.typeError = typeError;
    }

    public HttpStatus getTypeError(){
        return this.typeError;
    }
}
