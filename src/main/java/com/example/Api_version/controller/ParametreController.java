package com.example.Api_version.controller;

import com.example.Api_version.Services.ParametreService;
import com.example.Api_version.entities.Parametre;
import com.example.Api_version.request.ParametreRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api_licence/parametre/")
@CrossOrigin
public class ParametreController {
    private ParametreService parametreService;

    public ParametreController(ParametreService parametreService) {
        this.parametreService = parametreService;
    }
    @PostMapping("creer")
    public ResponseEntity<Parametre> create(@RequestBody ParametreRequest request){
        System.out.println(request);
        return new ResponseEntity<Parametre>(parametreService.creer(request), HttpStatus.CREATED);
    }
    @GetMapping("{id}")
    public ResponseEntity<Parametre> getOne(@PathVariable("id") int id){
        return ResponseEntity.ok(parametreService.getOne(id));
    }
    @GetMapping("all")
    public ResponseEntity<List<Parametre>> liste(){
        return ResponseEntity.ok(parametreService.lister());
    }
    @PutMapping("update/{id}")
    public ResponseEntity<Parametre> editer(@PathVariable("id") int id, @RequestBody ParametreRequest request){
        return ResponseEntity.ok(parametreService.editer(id,request));
    }
    @DeleteMapping("supprime/{id}/{dateCreation}")
    public ResponseEntity<Parametre> supprimer(@PathVariable("id") int id , @PathVariable("dateCreation") Date dateCreation){
        return ResponseEntity.ok(parametreService.supprimer(id, dateCreation));
    }
}
