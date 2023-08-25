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
@RequestMapping("produits")
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
    @GetMapping("/{code}")
    public ResponseEntity<Produit> read(@PathVariable("code") String code){
        return new ResponseEntity<Produit>(produitService.read(code), HttpStatus.OK);
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
    @PutMapping("/update/{code}")
    public ResponseEntity<Produit> update(@PathVariable("code") String code, @RequestBody ProduitRequest produitRequest){
        return new ResponseEntity<Produit>(produitService.update(code, produitRequest), HttpStatus.OK);
    }
    @DeleteMapping("/supprimer/{code}")
    public ResponseEntity<Produit> delete(@PathVariable("code") String code){
        return new ResponseEntity<Produit>(produitService.delete(code), HttpStatus.OK);
    }
}
