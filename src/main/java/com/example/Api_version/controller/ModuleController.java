package com.example.Api_version.controller;

import com.example.Api_version.Services.ModuleService;
import com.example.Api_version.entities.Module;
import com.example.Api_version.request.ModuleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api_licence/module/")
@CrossOrigin
public class ModuleController {
    private ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
    @PostMapping("creer")
    public ResponseEntity<Module> creer(@RequestBody ModuleRequest request){
        return new ResponseEntity<Module>(moduleService.creer(request), HttpStatus.CREATED);
    }
    @GetMapping("{id}")
    public ResponseEntity<Module> read(@PathVariable("id") int id){
        return new ResponseEntity<Module>(moduleService.read(id), HttpStatus.OK);
    }
    @GetMapping("all")
    public ResponseEntity<List<Module>> all(){
        return  ResponseEntity.ok().body(moduleService.all());
    }
    @GetMapping("all_deleted")
    public ResponseEntity<List<Module>> allDeleted(){
        return new ResponseEntity<List<Module>>(moduleService.allDeleted(), HttpStatus.OK);
    }
    @PutMapping("update/{id}")
    public ResponseEntity<Module> update(@PathVariable("id") int id,@RequestBody ModuleRequest request){
        return new ResponseEntity<Module>( moduleService.update(id,request), HttpStatus.OK);
    }
    @DeleteMapping("supprimer/{id}")
    public ResponseEntity<Module> delete(@PathVariable("id") int id){
        return new ResponseEntity<Module>(moduleService.delete(id), HttpStatus.OK);
    }
    @GetMapping("module_by_produit/{id}")
    public ResponseEntity<List<Module>> moduleByProduit(@PathVariable("id") int id){
        return new ResponseEntity<List<Module>>(moduleService.moduleByProduit(id), HttpStatus.OK);
    }
}
