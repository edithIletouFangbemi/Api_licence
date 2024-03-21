package com.example.Api_version.controller;

import com.example.Api_version.Services.ContratService;
import com.example.Api_version.entities.*;

import com.example.Api_version.entities.Module;
import com.example.Api_version.request.*;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api_licence/contrat/")
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

    @GetMapping("situationContrat")
    public ResponseEntity<List<Contrat_Institution>> situationContrat(
            @RequestParam(value = "institutionId", required = true) int institutionId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateDebut", required = true) Date dateDebut,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateFin", required = true) Date dateFin,
            @RequestParam(value = "typeContrat", required = true) String typeContrat,
            @RequestParam(value = "statut", required = true) int statut
    ){
        return ResponseEntity.ok(contratService.situationContrat(institutionId, typeContrat, statut, dateDebut, dateFin));
    }

    @GetMapping("sousContratByIdContrat/{idContrat}")
    public ResponseEntity<List<Sous_Contrat>> getAllSousContratByContratId(@PathVariable("idContrat") int idContrat){
        return ResponseEntity.ok(contratService.getAllByContrat(idContrat));
    }
    @GetMapping("detailSousContrat/{idAgence}")
    public ResponseEntity<List<DetailContrat>> getDetailContratByAgence(@PathVariable("idAgence") int idAgence){
        return ResponseEntity.ok(contratService.getDetailContratByAgence(idAgence));
    }
}
