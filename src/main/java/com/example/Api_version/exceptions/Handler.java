package com.example.Api_version.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlleur de Gestion des exception
 */
@RestControllerAdvice
public class Handler {
    @ExceptionHandler(InstitutionException.class)
    public ResponseEntity<?> InstitutionException(InstitutionException ex){
        return new  ResponseEntity<String>(ex.getMessage(), ex.getTypeError());
    }

    @ExceptionHandler(ExceptionWithCode.class)
    public ResponseEntity<?> ExceptionWithCodeHandler(ExceptionWithCode ex) {
       // ErrorResponse errorResponse = new ErrorResponse(ex.getTypeErreur().toString(), ex.getMessage());
        return new ResponseEntity<String>(ex.getMessage(), ex.getTypeErreur());
    }

    @ExceptionHandler(AgenceException.class)
    public ResponseEntity<?> AgenceException(AgenceException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ProduitException.class)
    public ResponseEntity<?> ProduitException(ProduitException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidError(MethodArgumentNotValidException ex){
        Map<String , String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error->{
            errorMap.put(error.getField(),error.getDefaultMessage());
        });
        return errorMap;
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = "Validation failed: ";
        errorMessage += ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " - " + error.getDefaultMessage())
                .reduce("", (str1, str2) -> str1 + ", " + str2);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleBindExceptions(BindException ex) {
        String errorMessage = "Validation failed: ";
        errorMessage += ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " - " + error.getDefaultMessage())
                .reduce("", (str1, str2) -> str1 + ", " + str2);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> UserException(UserException ex){
        return new ResponseEntity<String>(ex.getMessage(), ex.getTypeErreur());
    }
}
