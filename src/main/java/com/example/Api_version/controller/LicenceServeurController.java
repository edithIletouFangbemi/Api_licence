package com.example.Api_version.controller;

import com.example.Api_version.Services.LicenceServeurService;
import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.Institution;
import com.example.Api_version.request.LicenceReturnRequest;
import com.example.Api_version.request.LicenceReturnRequest2;
import com.example.Api_version.request.LicenceServeurRequest;
import com.example.Api_version.request.TestRequest;
import com.example.Api_version.utils.Jasperprint;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.PrintStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("licenceServeur/")
public class LicenceServeurController {

    private final LicenceServeurService licenceServeurService;
    Jasperprint jasperprint = new Jasperprint();
/*
    @PostMapping("creer")
    public ResponseEntity< LicenceReturnRequest2> creer(@RequestBody LicenceServeurRequest request) throws Exception {
        return new ResponseEntity<LicenceReturnRequest>((MultiValueMap<String, String>) licenceServeurService.creer(request),HttpStatus.CREATED);
    }*/
    @GetMapping("liste")
    public ResponseEntity<List<LicenceReturnRequest2 >> lister(){
        return ResponseEntity.ok(licenceServeurService.listeReturn());
    }
    @GetMapping("listeByInst/{codeInst}")
    public ResponseEntity<List<LicenceReturnRequest2>> listerByInst(@PathVariable("codeInst") String codeInst){
        return ResponseEntity.ok(licenceServeurService.licenceByInst(codeInst));
    }
    @GetMapping("liste-institution")
    public ResponseEntity<List<Institution>> listerInstitution(){
        return ResponseEntity.ok(licenceServeurService.listeDesInstitution());
    }

    @DeleteMapping("desactiver/{code}")
    public ResponseEntity<String> desactiver(@PathVariable("code") String code){
        return ResponseEntity.ok(licenceServeurService.deactivate(code));
    }
    @DeleteMapping("activate/{code}")
    public ResponseEntity<String> activate(@PathVariable("code") String code){
        return ResponseEntity.ok(licenceServeurService.activate(code));
    }

    @GetMapping("liste-agence/{code}")
    public ResponseEntity<List<Agence>> listeAgence(@PathVariable("code") String code){
        return ResponseEntity.ok(licenceServeurService.listeAgence(code));
    }
    @PostMapping("telecharger")
    public void downloadFiche(@RequestBody TestRequest request, HttpServletResponse response){
        jasperprint.print(licenceServeurService.doJasper(request), response);

    }

}
