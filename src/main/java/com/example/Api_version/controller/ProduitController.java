package com.example.Api_version.controller;

import com.example.Api_version.Services.ProduitService;
import com.example.Api_version.entities.Produit;
import com.example.Api_version.request.ProduitRequest;
import com.example.Api_version.request.ProduitReturnRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api_licence/produit")
@RequiredArgsConstructor
@CrossOrigin
public class ProduitController {
    @Autowired
    private ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @PostMapping("/create")
    public ResponseEntity<Produit> creer(@RequestBody ProduitRequest request){
        return new ResponseEntity<Produit>(produitService.creer(request), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Produit> read(@PathVariable("id") int id){
        return new ResponseEntity<Produit>(produitService.read(id), HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Produit>> all(){
        return new ResponseEntity<List<Produit>>(produitService.all(), HttpStatus.OK);
    }
    @GetMapping("/all_and_count")
    public ResponseEntity<List<ProduitReturnRequest>> listeAndCount(){
        return ResponseEntity.ok(produitService.lister());
    }
    @GetMapping("/all_deleted")
    public ResponseEntity<List<Produit>> allDeleted(){
        return new ResponseEntity<List<Produit>>(produitService.allDeleted(), HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Produit> update(@PathVariable("id") int id, @RequestBody ProduitRequest produitRequest){
        return new ResponseEntity<Produit>(produitService.update(id, produitRequest), HttpStatus.OK);
    }
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Produit> delete(@PathVariable("id") int id){
        return new ResponseEntity<Produit>(produitService.delete(id), HttpStatus.OK);
    }
}
