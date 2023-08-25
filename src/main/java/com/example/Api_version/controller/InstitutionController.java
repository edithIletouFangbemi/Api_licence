package com.example.Api_version.controller;

import com.example.Api_version.Services.InstitutionService;
import com.example.Api_version.entities.Institution;
import com.example.Api_version.repositories.InstitutionRepository;
import com.example.Api_version.request.InstitutionRequest;
import com.example.Api_version.request.InstitutionReturnRequest;
import com.example.Api_version.request.Statistique;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("institution/")
@RequiredArgsConstructor
@CrossOrigin
public class InstitutionController {
    private final InstitutionService institutionService;
    private final InstitutionRepository institutionRepository;

    @PostMapping("creer")
    public ResponseEntity<Institution> creer(@RequestBody InstitutionRequest request){
        return new ResponseEntity<Institution>(institutionService.creer(request), HttpStatus.CREATED);
    }
    @PutMapping("update/{code}")
    public ResponseEntity<Institution> update(@PathVariable("code") String code, @RequestBody InstitutionRequest institutionRequest){
        return new ResponseEntity<Institution>( institutionService.update(code, institutionRequest), HttpStatus.OK);
    }
    @GetMapping("getOne/{code}")
    public ResponseEntity<Institution> oneInstitution(@PathVariable("code") String code){
        return new ResponseEntity<Institution>(institutionService.OneInstitution(code), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<InstitutionReturnRequest>> liste(){
        return new ResponseEntity<List<InstitutionReturnRequest>>(institutionService.liste(),HttpStatus.ACCEPTED);
    }
    @GetMapping("all")
    public ResponseEntity<List<Institution>> all(){
        return ResponseEntity.ok(institutionRepository.findInstitutionByStatut(1));
    }
    @GetMapping("countByAgence")
    public ResponseEntity<List<Statistique>> countAgence(){
        return ResponseEntity.ok(institutionService.listeCountAgence());
    }
/*
    @GetMapping("listeSupprime")
    public ResponseEntity<List<InstitutionReturnRequest>> listeDeleted(){
        return new ResponseEntity<List<InstitutionReturnRequest>>(institutionService.listeDeleted(),HttpStatus.ACCEPTED);
    }

 */
    @DeleteMapping("supprime/{code}")
    public ResponseEntity<String> delete(@PathVariable("code") String code){
        return new ResponseEntity<String>(institutionService.supprimer(code), HttpStatus.OK);
    }


}
