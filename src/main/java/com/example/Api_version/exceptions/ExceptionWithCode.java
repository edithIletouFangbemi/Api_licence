package com.example.Api_version.exceptions;

import org.springframework.http.HttpStatus;

public class ExceptionWithCode extends RuntimeException{
    private HttpStatus typeErreur;

    public ExceptionWithCode(HttpStatus typeErreur, String message) {
        super(message);
        this.typeErreur = typeErreur;
    }

    public HttpStatus getTypeErreur() {
        return typeErreur;
    }
}
