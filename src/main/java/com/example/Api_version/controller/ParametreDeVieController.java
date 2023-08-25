package com.example.Api_version.controller;

import com.example.Api_version.Services.ParametreDevieService;
import com.example.Api_version.entities.ParametreDeVieLicence;
import com.example.Api_version.request.ParametreDeVieRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("parametre-vie/")
@CrossOrigin
public class ParametreDeVieController {
    private final ParametreDevieService parametreDevieService;
    @PostMapping("create")
    public ResponseEntity<ParametreDeVieLicence> creer(@RequestBody ParametreDeVieRequest request){
        return new ResponseEntity<ParametreDeVieLicence>(parametreDevieService.creer(request), HttpStatus.CREATED);
    }
    @GetMapping("read/{id}")
    public ResponseEntity<ParametreDeVieLicence> read(@PathVariable("id") int id){
        return ResponseEntity.ok(parametreDevieService.read(id));
    }
    @GetMapping("all")
    public ResponseEntity<List<ParametreDeVieLicence>> all(){
        return ResponseEntity.ok(parametreDevieService.findAll());
    }
    @PutMapping("update/{id}")
    public ResponseEntity<ParametreDeVieLicence> update(@PathVariable("id") int id, @RequestBody ParametreDeVieRequest request){
        return ResponseEntity.ok(parametreDevieService.update(id, request));
    }
    @DeleteMapping("deactivate/{id}")
    public ResponseEntity<ParametreDeVieLicence> deactivate(@PathVariable("id") int id){
        return ResponseEntity.ok(parametreDevieService.désactiver(id));
    }
    @DeleteMapping("activate/{id}")
    public ResponseEntity<ParametreDeVieLicence> activate(@PathVariable("id") int id){
        return ResponseEntity.ok(parametreDevieService.activer(id));
    }
}
