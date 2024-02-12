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

/**
 * Controlleur de Gestion des Institutions
 */
@RestController
@RequestMapping("institution/")
@RequiredArgsConstructor
@CrossOrigin
public class InstitutionController {
    private final InstitutionService institutionService;
    private final InstitutionRepository institutionRepository;

    /**
     * Créer une institution
     * @param request
     * @return l'institution créée
     */
    @PostMapping("creer")
    public ResponseEntity<Institution> creer(@RequestBody InstitutionRequest request){
        return new ResponseEntity<Institution>(institutionService.creer(request), HttpStatus.CREATED);
    }

    /**
     * Mettre à jour une institution
     * @param id
     * @param institutionRequest
     * @return retourner l'institution mise à jour
     */
    @PutMapping("update/{id}")
    public ResponseEntity<Institution> update(@PathVariable("id") int id, @RequestBody InstitutionRequest institutionRequest){
        return new ResponseEntity<Institution>( institutionService.update(id, institutionRequest), HttpStatus.OK);
    }
    @GetMapping("getOne/{id}")
    public ResponseEntity<Institution> oneInstitution(@PathVariable("id") int id){
        return new ResponseEntity<Institution>(institutionService.OneInstitution(id), HttpStatus.OK);
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

    @GetMapping("countWithProduit")
    public ResponseEntity<List<Statistique>> listeDeleted(){
        return ResponseEntity.ok(institutionService.countWithProduit());
    }

    @DeleteMapping("supprime/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id){
        return new ResponseEntity<String>(institutionService.supprimer(id), HttpStatus.OK);
    }


}
