package com.example.Api_version.Services;

import com.example.Api_version.entities.Module;
import com.example.Api_version.entities.Produit;
import com.example.Api_version.entities.TypeModule;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.ModuleRepository;
import com.example.Api_version.repositories.ProduitRepository;
import com.example.Api_version.request.ModuleRequest;
import com.example.Api_version.utils.CodeGenerator;
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
        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(moduleRequest.getProduitId(),1);
        if(produitOptional.isEmpty()) throw new ProduitException("aucun produit avec l'identifiant "+moduleRequest.getProduitId());
        Produit produit = produitOptional.get();
        nbr = 0;
        moduleByProduit(moduleRequest.getProduitId()).forEach(x->{
            if(x.getTypeModule() =="standard"){
               nbr++;
            }
        });

        if((moduleRequest.getTypeModule().toLowerCase()=="additionnel") && (nbr == 0)){
            throw new ProduitException(produit.getNom()+" n'as pas encore de module standard donc impossible d'ajouter un module additionnel");
        }

        Optional<Module> moduleOptional = moduleRepository.findByLibelleModuleAndProduit(moduleRequest.getLibelleModule().toUpperCase(),produit);
        Optional<Module> moduleStandard = moduleRepository.findByProduitAndTypeModuleAndStatut(produit,"standard", 1);
        if(moduleStandard.isPresent()){
            if(moduleStandard.get().getTypeModule().equals(moduleRequest.getTypeModule())) throw new ProduitException("CE produit a deja un module Standard!!");
        }

        if (moduleOptional.isPresent()){
            module = moduleOptional.get();
            if(module.getStatut() == 1) throw new ProduitException(produit.getNom()+" a deja un module avec le nom "
                    +moduleRequest.getLibelleModule());
            module.setDescription(moduleRequest.getDescription());
            module.setDateCreation(now());
            module.setDateSuppression(null);
            module.setStatut(1);
            module.setTypeModule(moduleRequest.getTypeModule().toLowerCase());
            module.setProduit(produitOptional.get());
            moduleRepository.save(module);

        }
        module = new Module();
        module.setCodeModule(CodeGenerator.codeUser(moduleRequest.getLibelleModule(),produit.getCodeProduit()));
        module.setDescription(moduleRequest.getDescription());
        module.setDateCreation(now());
        module.setStatut(1);
        module.setLibelleModule(moduleRequest.getLibelleModule().toUpperCase());
        module.setTypeModule(moduleRequest.getTypeModule().toLowerCase());
        module.setProduit(produit);
        return moduleRepository.save(module);


    }

    public Module read(String code){
        Optional<Module> moduleOptional = moduleRepository.findByCodeModuleAndStatut(code,1);
        if(moduleOptional.isEmpty()) throw new ProduitException("aucun module avec l'identifiant "+ code);
        return moduleOptional.get();
    }

    public List<Module> all(){
        return moduleRepository.findAllByStatut(1);
    }

    public List<Module> allDeleted(){
        return moduleRepository.findAllByStatut(2);
    }

    public Module update(String code, ModuleRequest request){
        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(request.getProduitId(), 1);
        if(produitOptional.isEmpty()) throw new ProduitException("aucun produit avec l'identifiant "+ request.getProduitId());
        Produit produit = produitOptional.get();
        Optional<Module> moduleOptional = moduleRepository.findByCodeModuleAndProduitAndStatut(code,produit,1);
        if(moduleOptional.isEmpty()) throw new ProduitException("aucun module du produit "+produit.getNom()+" avec l'identifiant "+ code);
        module = moduleOptional.get();
        module.setLibelleModule(request.getLibelleModule().toUpperCase());
        module.setTypeModule(request.getTypeModule());
        module.setProduit(produit);

        return moduleRepository.save(module);

    }

    public Module delete(String code){
        Optional<Module> moduleOptional = moduleRepository.findByCodeModuleAndStatut(code,1);
        if(moduleOptional.isEmpty()) throw new ProduitException("aucun module avec le code "+ code);
        module = moduleOptional.get();
        module.setStatut(2);
        module.setDateSuppression(now());


        return moduleRepository.save(module);
    }

    public List<Module> moduleByProduit(String code){
        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(code,1);
        if(produitOptional.isEmpty()) throw new ProduitException("aucun produit avec le code "+code);
        return moduleRepository.findAllByProduitAndStatut(produitOptional.get(),1);
    }
}
