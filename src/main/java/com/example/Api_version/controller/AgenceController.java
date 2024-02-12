package com.example.Api_version.controller;

import com.example.Api_version.Services.AgenceService;
import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.Module;
import com.example.Api_version.request.ActivationRequest;
import com.example.Api_version.request.ActiverProduitRequest;
import com.example.Api_version.request.ActiverRequest;
import com.example.Api_version.request.AgenceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("agence/")
@CrossOrigin
public class AgenceController {
    private AgenceService agenceService;

    public AgenceController(AgenceService agenceService) {
        this.agenceService = agenceService;
    }
    @PostMapping("creer")
    public ResponseEntity<Agence> creer(@RequestBody AgenceRequest request){
        return new ResponseEntity<Agence>(agenceService.creer(request), HttpStatus.ACCEPTED);
    }
    @GetMapping("getOne/{code}")
    public ResponseEntity<Agence> getOne(@PathVariable("code") String code){
        return new ResponseEntity<Agence>( agenceService.uneAgence(code), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Agence>>all(){
        return new ResponseEntity<List<Agence>>(agenceService.liste(), HttpStatus.OK);
    }

    @GetMapping("listeSupprime")
    public ResponseEntity<List<Agence>> allDeleted(){
        return new ResponseEntity<List<Agence>>(agenceService.ListeDeleted(),HttpStatus.OK);
    }
    @PutMapping("update/{code}")
    public ResponseEntity<Agence> update(@PathVariable("code") String code,@RequestBody AgenceRequest request){
        return new ResponseEntity<Agence>(agenceService.update(code,request), HttpStatus.OK);
    }
    @DeleteMapping("delete/{code}")
    public ResponseEntity<String> delete(@PathVariable("code") String code){
        return new ResponseEntity<String>( agenceService.supprimer(code), HttpStatus.OK);
    }
    @GetMapping("agenceByInstitution/{code}")
    public ResponseEntity<List<Agence>> allAgence(@PathVariable("code") String code){
        return new ResponseEntity<List<Agence>>(agenceService.ListeParInstitution(code), HttpStatus.OK);
    }
    @GetMapping("modules/{codeagence}/{codeproduit}")
    public ResponseEntity<List<Module>> modules(@PathVariable("codeagence") String codeagence, @PathVariable("codeproduit") String codeproduit){
        return ResponseEntity.ok(agenceService.moduleList(codeagence,codeproduit));
    }
    @PostMapping("activation")
    public ResponseEntity<Agence> activerModule(@RequestBody ActivationRequest request){
        return new ResponseEntity<Agence>(agenceService.activerModule(request), HttpStatus.CREATED);
    }

    @PostMapping("activation-produit")
    public ResponseEntity<Agence> activerProduit(@RequestBody ActiverProduitRequest request){
        return new ResponseEntity<Agence>(agenceService.activerProduit(request.getCodeproduit(), request.getCodeagence()), HttpStatus.CREATED);
    }
}
