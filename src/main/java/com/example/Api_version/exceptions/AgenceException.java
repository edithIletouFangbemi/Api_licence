package com.example.Api_version.exceptions;

import org.springframework.http.HttpStatus;

public class AgenceException extends RuntimeException{

    private HttpStatus typeErreur;
    public AgenceException(String message, HttpStatus typeErreur){

        super(message);
        this.typeErreur = typeErreur;
    }

    public HttpStatus getTypeErreur() {
        return typeErreur;
    }
}
