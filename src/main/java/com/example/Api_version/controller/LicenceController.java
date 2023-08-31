package com.example.Api_version.controller;

import com.example.Api_version.Services.LicenceService;
import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.Institution;
import com.example.Api_version.entities.Module;
import com.example.Api_version.entities.Produit;
import com.example.Api_version.request.*;
import com.example.Api_version.utils.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("licence/")
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
    public ResponseEntity<List<LicenceReturnRequest>> licence(@RequestBody LicenceRequest request) throws Exception {
        return new ResponseEntity<List<LicenceReturnRequest>>(licenceService.genererLicence(request), HttpStatus.CREATED);
    }

    @GetMapping("recapLicence")
    public ResponseEntity<List<LicenceRecapRequest>> lister(){
        return ResponseEntity.ok(licenceService.recapLicence());
    }
    @GetMapping("unelicence/codeLicence")
    public ResponseEntity<LicenceReturnRequest> getLicence(@PathVariable("codeLicence") String codeLicence) throws Exception {
        return ResponseEntity.ok(licenceService.getLicence(codeLicence));
    }

    @GetMapping("telecharger/{codeLicence}")
    public void download(@PathVariable("codeLicence") String codeLicence){
        licenceService.download(codeLicence);
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

}


