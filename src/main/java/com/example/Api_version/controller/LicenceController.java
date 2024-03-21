package com.example.Api_version.controller;

import com.example.Api_version.Services.LicenceService;
import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import com.example.Api_version.request.*;
import com.example.Api_version.utils.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api_licence/licence/")
@RequiredArgsConstructor
public class LicenceController {
    private final LicenceService licenceService;
    @GetMapping("institutions")
    public ResponseEntity<List<Institution>> institutions(){
        return ResponseEntity.ok(licenceService.institutionList());
    }
    @GetMapping("agences/{codeinst}/{codeproduit}")
    public List<Agence> agences(@PathVariable("codeinst") String codeinst, @PathVariable("codeproduit") String codeproduit){
        return licenceService.agenceList(codeinst,codeproduit);
    }

    @GetMapping("produits/{codeinst}")
    public ResponseEntity<List<Produit>> produits(@PathVariable("codeinst") String codeinst){
        return ResponseEntity.ok(licenceService.produitList(codeinst));
    }
    @GetMapping("modules/{codeagence}/{codeproduit}")
    public ResponseEntity<List<Module>> modules(@PathVariable("codeagence") String codeagence, @PathVariable("codeproduit") String codeproduit){
        return ResponseEntity.ok(licenceService.moduleList(codeagence,codeproduit));
    }
    @PostMapping("generer")
    public ResponseEntity<List<LicenceReturnRequest2>> licence(@RequestBody LicenceRequest request) throws Exception {
        return new ResponseEntity<List<LicenceReturnRequest2>>(licenceService.genererLicence(request), HttpStatus.CREATED);
    }

    @GetMapping("recapLicence")
    public ResponseEntity<List<LicenceRecapRequest>> lister(){
        return ResponseEntity.ok(licenceService.recapLicence());
    }
    @GetMapping("unelicence/codeLicence")
    public ResponseEntity<LicenceReturnRequest> getLicence(@PathVariable("codeLicence") String codeLicence) throws Exception {
        return ResponseEntity.ok(licenceService.downloadLicence(codeLicence));
    }

    @GetMapping("telecharger/{idLicence}")
    public ResponseEntity<DownloadRequest> download(@PathVariable("idLicence") int idLicence){
       return ResponseEntity.ok(licenceService.download(idLicence));
    }

    @GetMapping("all-active")
    public ResponseEntity<Integer> size(){
        return ResponseEntity.ok(licenceService.allLicence());
    }

    @PostMapping("ActiveLicence")
    public ResponseEntity<CheckReturn> checkAndActiveLicence(@RequestBody LicenceReturnRequest request) throws Exception {
        return ResponseEntity.ok(licenceService.checkLicence(request));
    }
    @GetMapping("recap-agence/{codeinst}")
    public ResponseEntity<List<AgenceRecapRequest>> recapAgence(@PathVariable("codeinst") String codeinst){
        return ResponseEntity.ok(licenceService.recapAgence(codeinst));
    }
    @GetMapping("recap-poste/{codeagence}")
    public ResponseEntity<List<PosteDetailRequest>> recapPoste(@PathVariable("codeagence") String codeagence){
        return ResponseEntity.ok(licenceService.recapPoste(codeagence));
    }
    @GetMapping("situationLicence")
    public ResponseEntity<List<SituationLicenceRequest>> situationLicence(
            @RequestParam(value = "agenceId", required = true) int agenceId,
            @RequestParam(value = "posteIds", required = true) List<Integer> posteIds,
            @RequestParam(value = "moduleIds", required = true) List<Integer> moduleIds,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateDebut", required = true) Date dateDebut,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "dateFin", required = true) Date dateFin,
            @RequestParam(value = "typeLicence", required = true) String typeLicence,
            @RequestParam(value = "statut", required = true) int statut
    ){
        return ResponseEntity.ok(licenceService.situationLicence(
                agenceId, posteIds, moduleIds, typeLicence, dateDebut, dateFin, statut
        ));
    }
    @GetMapping("allPostesByAgence/{agenceId}")
    public ResponseEntity<List<Poste>> getAllPosteByAgence(@PathVariable("agenceId") int agenceId){
        return ResponseEntity.ok(licenceService.getAllPosteByAgenceAndStatut(agenceId));
    }
    @GetMapping("licencesForDownload")
    public ResponseEntity<List<LicenceReturnRequest2>> getLicencesForDownload(
            @RequestParam(value = "listeIdLicence" , required = true) List<Integer> listeIdlicence,
            @RequestParam(value = "typeLicence" , required = true) String typeLicence
    ){
        return ResponseEntity.ok(licenceService.getLicencesForDownload(listeIdlicence, typeLicence));
    }
    @DeleteMapping("activerLicence/{idLicence}")
    public ResponseEntity<LicenceReturnRequest> activerLicence(@PathVariable("idLicence") int idLicence){
        return ResponseEntity.ok(licenceService.activerLicence(idLicence));
    }

    @DeleteMapping("desactiverLicence/{idLicence}")
    public ResponseEntity<LicenceReturnRequest> desactiverLicence(@PathVariable("idLicence") int idLicence){
        return ResponseEntity.ok(licenceService.desactiverLicence(idLicence));
    }

}


