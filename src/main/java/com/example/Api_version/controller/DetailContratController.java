package com.example.Api_version.controller;

import com.example.Api_version.Services.DetailContratService;
import com.example.Api_version.entities.DetailContrat;
import com.example.Api_version.request.DetailContratRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api_licence/detail_contrat/")
@RequiredArgsConstructor
@CrossOrigin
public class DetailContratController {
    private final DetailContratService detailContratService;
    @PostMapping("creer")
    public ResponseEntity<DetailContrat> creer(@RequestBody DetailContratRequest request){
        return new ResponseEntity<DetailContrat>(detailContratService.creer(request), HttpStatus.CREATED);
    }
    @GetMapping("all")
    public ResponseEntity<List<DetailContrat>> lister(){
        return ResponseEntity.ok(detailContratService.liste());
    }
    @GetMapping("getOne/{id}")
    public ResponseEntity<DetailContrat> getOne(@PathVariable("id") int id){
        return ResponseEntity.ok(detailContratService.getOne(id));
    }
    @GetMapping("allByAgence/{idAgence}")
    public ResponseEntity<List<DetailContrat>> getAllByAgence(@PathVariable("idAgence") int idAgence){
        return ResponseEntity.ok(detailContratService.getAllByAgence(idAgence));
    }
}
