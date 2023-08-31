package com.example.Api_version.controller;

import com.example.Api_version.Services.ContratService;
import com.example.Api_version.entities.Contrat_Institution;
import com.example.Api_version.entities.Institution;
import com.example.Api_version.entities.Produit;

import com.example.Api_version.request.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.Api_version.entities.Module;

import java.util.List;

@Controller
@RequestMapping("/contrat/")
@CrossOrigin
public class ContratController {
    private ContratService contratService;

    public ContratController(ContratService contratService) {
        this.contratService = contratService;
    }

    @PostMapping("creer")
    public ResponseEntity<Institution> creer(@RequestBody Contrat request){
        return new ResponseEntity<Institution>(contratService.creerContrat(request),
                HttpStatus.CREATED);
    }
    @GetMapping("all/{codeinst}")
    public ResponseEntity<List<Contrat_Institution>> all(@PathVariable("codeinst") String codeinst){
        return  ResponseEntity.ok(contratService.allByInstitution(codeinst));
    }
    @PostMapping("activerModule")
    public ResponseEntity<Institution> activerMod(@RequestBody String codeinst){
        return new ResponseEntity<Institution>(contratService.activerModule(codeinst), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DetailContratInst>> liste(){
        return ResponseEntity.ok(contratService.lister());
    }
    @GetMapping("getone/{code}")
    public ResponseEntity<Contrat_Institution> getOne(@PathVariable("code") String code){
        return ResponseEntity.ok(contratService.getOne(code));
    }
    @GetMapping("liste")
    public ResponseEntity<List<Institution>> listeByInstitution(){
        return ResponseEntity.ok(contratService.listeInstitution());
    }
    @GetMapping("institutions")
    public ResponseEntity<List<Institution>> listeDesInstitution(){
        return ResponseEntity.ok(contratService.uneListeInstitution());
    }
    @GetMapping("produit/{code}")
    public ResponseEntity<List<Produit>> listerByProduit(@PathVariable("code") String code){
        return ResponseEntity.ok(contratService.listeProduitParInstitution(code));
    }
    @PostMapping("getByInstitution")
    public ResponseEntity<Contrat_Institution> getContrat(@RequestBody Institution institution){
        return ResponseEntity.ok(contratService.getByInstitution(institution));
    }

    @GetMapping("listeModule/{codeProduit}")
    public ResponseEntity<List<Module>> listerModule(@PathVariable("codeProduit") String codeProduit){
        return ResponseEntity.ok(contratService.listemoduleByProduit(codeProduit));
    }

    @GetMapping("detailAgence/{codeinst}/{codeagence}")
    public ResponseEntity<AgenceDetail> detail(@PathVariable("codeinst") String codeinst, @PathVariable("codeagence") String codeagence){
        return ResponseEntity.ok(contratService.detailagence(codeinst, codeagence));
    }
    /*
    @PostMapping("ajoutAvenant")
    public ResponseEntity<Contrat_Institution> ajouterAvenant(@RequestBody ContratUnit contratUnit){
        return new ResponseEntity<Contrat_Institution>(contratService.ajoutAvenant(contratUnit), HttpStatus.CREATED);
    } */
    @GetMapping("produitPourContrat/{codeinst}")
    public ResponseEntity<List<Produit>> productsForNewContrat(@PathVariable("codeinst") String codeinst){
        return ResponseEntity.ok(contratService.productsByInst(codeinst));
    }
}
