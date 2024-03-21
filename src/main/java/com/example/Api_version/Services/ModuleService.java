package com.example.Api_version.Services;

import com.example.Api_version.entities.Module;
import com.example.Api_version.entities.Produit;
import com.example.Api_version.entities.TypeModule;
import com.example.Api_version.exceptions.AgenceException;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.ModuleRepository;
import com.example.Api_version.repositories.ProduitRepository;
import com.example.Api_version.request.ModuleRequest;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@Transactional
public class ModuleService {
    private ModuleRepository moduleRepository;
    private Module module;
    private ProduitRepository produitRepository;
    private int nbr;

    public ModuleService(ModuleRepository moduleRepository, ProduitRepository produitRepository) {
        this.moduleRepository = moduleRepository;
        this.produitRepository = produitRepository;
    }

    public Module creer(ModuleRequest moduleRequest){
        Optional<Produit> produitOptional = produitRepository.findByIdAndStatut(moduleRequest.getProduitId(),1);
        if(produitOptional.isEmpty()) throw new AgenceException(" aucun produit avec l'identifiant "+moduleRequest.getProduitId(),HttpStatus.NOT_FOUND);
        Produit produit = produitOptional.get();
        nbr = 0;
        moduleByProduit(moduleRequest.getProduitId()).forEach(x->{
            if(x.getTypeModule() =="standard"){
               nbr++;
            }
        });

        if((moduleRequest.getTypeModule().toLowerCase()=="additionnel") && (nbr == 0)){
            throw new AgenceException(produit.getNom()+" n'as pas encore de module standard donc impossible d'ajouter un module additionnel",HttpStatus.NOT_FOUND);
        }

        Optional<Module> moduleOptional = moduleRepository.findByLibelleModuleIgnoreCaseAndProduit(moduleRequest.getLibelleModule().toUpperCase(),produit);
        Optional<Module> modulOptionalCheckCode = moduleRepository.findByCodeModuleIgnoreCaseAndProduitAndStatut(moduleRequest.getCodeModule(),produit,1);
      //  Optional<Module> moduleStandard = moduleRepository.findByProduitAndTypeModuleAndStatut(produit,"standard", 1);

       /* if(moduleStandard.isPresent()){
            if(moduleStandard.get().getTypeModule().equals(moduleRequest.getTypeModule())) throw new ProduitException("CE produit a deja un module Standard!!");
        }*/

        if(modulOptionalCheckCode.isPresent()) throw new AgenceException(" un module avec le code "+ moduleRequest.getCodeModule()+" de "+produit.getNom()+" existe dejà!", HttpStatus.BAD_REQUEST);

        if(moduleOptional.isPresent()) throw new AgenceException(" un module avec le nom "+ moduleRequest.getLibelleModule()+" de "+produit.getNom()+" existe dejà!",HttpStatus.BAD_REQUEST);


        if (moduleOptional.isPresent()){
            module = moduleOptional.get();
            if(module.getStatut() == 1) throw new AgenceException(produit.getNom()+" a deja un module avec le nom "
                    +moduleRequest.getLibelleModule(),HttpStatus.ALREADY_REPORTED);
            module.setDescription(moduleRequest.getDescription());
            module.setDateCreation(now());
            module.setDateSuppression(null);
            module.setStatut(1);
            module.setTypeModule(moduleRequest.getTypeModule().toLowerCase());
            module.setProduit(produitOptional.get());
            moduleRepository.save(module);

        }

        module = new Module();
        module.setCodeModule(moduleRequest.getCodeModule());
        module.setDescription(moduleRequest.getDescription());
        module.setDateCreation(now());
        module.setStatut(1);
        module.setLibelleModule(moduleRequest.getLibelleModule().toUpperCase());
        module.setTypeModule(moduleRequest.getTypeModule().toLowerCase());
        module.setProduit(produit);
        return moduleRepository.save(module);

    }

    public Module read(int id){
        Optional<Module> moduleOptional = moduleRepository.findByIdAndStatut(id,1);
        if(moduleOptional.isEmpty()) throw new AgenceException("aucun module avec l'identifiant "+ id,HttpStatus.NOT_FOUND);
        return moduleOptional.get();
    }

    public List<Module> all(){
        return moduleRepository.findAllByStatut(1);
    }

    public List<Module> allDeleted(){
        return moduleRepository.findAllByStatut(2);
    }

    public Module update(int id, ModuleRequest request){
        Optional<Produit> produitOptional = produitRepository.findByIdAndStatut(request.getProduitId(), 1);
        if(produitOptional.isEmpty()) throw new AgenceException("aucun produit avec l'identifiant "+ request.getProduitId(), HttpStatus.NOT_FOUND);
        Produit produit = produitOptional.get();
        Optional<Module> moduleOptional = moduleRepository.findByIdAndStatut(id,1);
        Optional<Module> moduleOptionalCheckWithName = moduleRepository.findByLibelleModuleIgnoreCaseAndProduitAndStatut(request.getLibelleModule(),produit,1);
        Optional<Module> moduleOptionalCheckWithCode = moduleRepository.findByCodeModuleIgnoreCaseAndProduitAndStatut(request.getCodeModule(),produit,1);
        if(moduleOptional.isEmpty()) throw new AgenceException("aucun module du produit "+produit.getNom()+" avec l'identifiant "+ id,HttpStatus.NOT_FOUND);
        if(moduleOptionalCheckWithCode.isPresent()) throw new AgenceException("un module du produit "+produit.getNom()+" existe deja avec le code "+request.getCodeModule(), HttpStatus.ALREADY_REPORTED);
        if(moduleOptionalCheckWithName.isPresent()) throw new AgenceException("un module de "+produit.getNom()+" existe deja avec le libelle "+request.getLibelleModule(), HttpStatus.ALREADY_REPORTED);

        module = moduleOptional.get();
        module.setLibelleModule(request.getLibelleModule().toUpperCase());
        module.setCodeModule(request.getCodeModule());
        module.setTypeModule(request.getTypeModule());
        module.setProduit(produit);

        return moduleRepository.save(module);
    }

    public Module delete(int id){
        Optional<Module> moduleOptional = moduleRepository.findByIdAndStatut(id,1);
        if(moduleOptional.isEmpty()) throw new AgenceException("aucun module avec le code "+ id,HttpStatus.NOT_FOUND);
        module = moduleOptional.get();
        module.setStatut(2);
        module.setDateSuppression(now());

        return moduleRepository.save(module);
    }

    public List<Module> moduleByProduit(int id){
        Optional<Produit> produitOptional = produitRepository.findByIdAndStatut(id,1);
        if(produitOptional.isEmpty()) throw new AgenceException("aucun produit avec l'identifiant "+id,HttpStatus.NOT_FOUND);
        return moduleRepository.findAllByProduitAndStatut(produitOptional.get(),1);
    }
}
