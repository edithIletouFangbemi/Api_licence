package com.example.Api_version.controller;

import com.example.Api_version.Services.SousHabilitationService;
import com.example.Api_version.entities.SousHabilitation;
import com.example.Api_version.request.SousHabilitationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api_licence/soushabilitation/")
@RequiredArgsConstructor
public class SousHabilitationController {
    private final SousHabilitationService sousHabilitationService;
    @PostMapping("creer")
    private ResponseEntity<SousHabilitation> creerSousHabilitation(@RequestBody SousHabilitationRequest request){
        return new ResponseEntity<SousHabilitation>(sousHabilitationService.creer(request), HttpStatus.CREATED);
    }
    @GetMapping("ByHabilitation/{codeHabilitation}")
    public ResponseEntity<List<SousHabilitation>> liste(@PathVariable("codeHabilitation") String codeHabilitation){
        return ResponseEntity.ok(sousHabilitationService.listeSousHabilitation(codeHabilitation));
    }
}
