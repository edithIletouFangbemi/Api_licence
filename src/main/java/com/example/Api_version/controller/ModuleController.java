package com.example.Api_version.controller;

import com.example.Api_version.Services.ModuleService;
import com.example.Api_version.entities.Module;
import com.example.Api_version.request.ModuleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("module/")
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
    @GetMapping("{code}")
    public ResponseEntity<Module> read(@PathVariable("code") String code){
        return new ResponseEntity<Module>(moduleService.read(code), HttpStatus.OK);
    }
    @GetMapping("all")
    public ResponseEntity<List<Module>> all(){
        return  ResponseEntity.ok().body(moduleService.all());
    }
    @GetMapping("all_deleted")
    public ResponseEntity<List<Module>> allDeleted(){
        return new ResponseEntity<List<Module>>(moduleService.allDeleted(), HttpStatus.OK);
    }
    @PutMapping("update/{code}")
    public ResponseEntity<Module> update(@PathVariable("code") String code,@RequestBody ModuleRequest request){
        return new ResponseEntity<Module>( moduleService.update(code,request), HttpStatus.OK);
    }
    @DeleteMapping("supprimer/{code}")
    public ResponseEntity<Module> delete(@PathVariable("code") String code){
        return new ResponseEntity<Module>(moduleService.delete(code), HttpStatus.OK);
    }
    @GetMapping("module_by_produit/{code}")
    public ResponseEntity<List<Module>> moduleByProduit(@PathVariable("code") String code){
        return new ResponseEntity<List<Module>>(moduleService.moduleByProduit(code), HttpStatus.OK);
    }
}
