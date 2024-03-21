package com.example.Api_version.controller;

import com.example.Api_version.Services.HabilitationService;
import com.example.Api_version.entities.Habilitation;
import com.example.Api_version.request.HabilitationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api_licence/habilitation/")
@CrossOrigin
public class HabilitationController {
    private HabilitationService habilitationService;

    public HabilitationController(HabilitationService habilitationService) {
        this.habilitationService = habilitationService;
    }
    @PostMapping("creer")
    public ResponseEntity<Habilitation> creer(@RequestBody HabilitationRequest request){
        return new ResponseEntity<Habilitation>(habilitationService.creer(request), HttpStatus.CREATED);
    }
    @GetMapping("{code}")
    public ResponseEntity<Habilitation> getOne(@PathVariable("code") String code){
        return new ResponseEntity<Habilitation>(habilitationService.one(code), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Habilitation>> liste(){
        return new ResponseEntity<List<Habilitation>>(habilitationService.liste(), HttpStatus.OK);
    }
    @PutMapping("update/{code}")
    public ResponseEntity<Habilitation> update(@PathVariable("code") String code, @RequestBody HabilitationRequest request){
        return new ResponseEntity<Habilitation>(habilitationService.update(code, request), HttpStatus.OK);
    }
    @DeleteMapping("delete/{code}")
    public ResponseEntity<String> delete(@PathVariable("code") String code){
        return new ResponseEntity<String>(habilitationService.delete(code), HttpStatus.OK);
    }

    @DeleteMapping("activer/{code}")
    public ResponseEntity<String> activer(@PathVariable("code") String code){
        return ResponseEntity.ok(habilitationService.activer(code));
    }

}
