package com.example.Api_version.controller;

import com.example.Api_version.Services.ProfilService;
import com.example.Api_version.entities.Profil;
import com.example.Api_version.request.ProfilRequest;
import com.example.Api_version.request.ProfilReturnRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("profil/")
@CrossOrigin
public class ProfilController {
    private ProfilService profilService;

    private Profil profil;

    public ProfilController(ProfilService profilService) {
        this.profilService = profilService;
    }
    @PostMapping("creer")
    public ResponseEntity<Profil> creer(@RequestBody ProfilRequest request){
        return new ResponseEntity<Profil>(profilService.creer(request), HttpStatus.CREATED);
    }
    @GetMapping("all")
    public ResponseEntity<List<Profil>> liste(){
        return ResponseEntity.ok(profilService.liste());
    }
    @PutMapping("update/{code}")
    public ResponseEntity<Profil> update(@PathVariable("code") String code, @RequestBody ProfilRequest request){
        return ResponseEntity.ok(profilService.update(code, request));
    }
    @DeleteMapping("desactiver/{code}")
    public ResponseEntity<String> delete(@PathVariable("code") String code){
        return ResponseEntity.ok(profilService.delete(code));
    }

    @DeleteMapping("activer/{code}")
    public ResponseEntity<String> activer(@PathVariable("code") String code){
        return ResponseEntity.ok(profilService.delete(code));
    }


}
