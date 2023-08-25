package com.example.Api_version.controller;

import com.example.Api_version.Services.ParametreService;
import com.example.Api_version.entities.Parametre;
import com.example.Api_version.request.ParametreRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("parametre/")
@CrossOrigin
public class ParametreController {
    private ParametreService parametreService;

    public ParametreController(ParametreService parametreService) {
        this.parametreService = parametreService;
    }
    @PostMapping("creer")
    public ResponseEntity<Parametre> create(@RequestBody ParametreRequest request){
        return new ResponseEntity<Parametre>(parametreService.creer(request), HttpStatus.CREATED);
    }
    @GetMapping("{code}")
    public ResponseEntity<Parametre> getOne(@PathVariable("code") String code){
        return ResponseEntity.ok(parametreService.getOne(code));
    }
    @GetMapping("all")
    public ResponseEntity<List<Parametre>> liste(){
        return ResponseEntity.ok(parametreService.lister());
    }
    @PutMapping("update/{code}")
    public ResponseEntity<Parametre> editer(@PathVariable("code") String code, @RequestBody ParametreRequest request){
        return ResponseEntity.ok(parametreService.editer(code,request));
    }
    @DeleteMapping("supprime/{code}")
    public ResponseEntity<Parametre> supprimer(@PathVariable("code") String code){
        return ResponseEntity.ok(parametreService.supprimer(code));
    }
}
