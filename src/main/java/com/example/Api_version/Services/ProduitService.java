package com.example.Api_version.Services;

import com.example.Api_version.entities.Produit;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.ProduitRepository;
import com.example.Api_version.request.ProduitRequest;
import com.example.Api_version.request.ProduitReturnRequest;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@Transactional
public class ProduitService {
    private ProduitRepository produitRepository;
    private Produit product = new Produit();

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public Produit creer(ProduitRequest request){
        Optional<Produit> produitOptional = produitRepository.findByNom(request.getNom().toUpperCase());
        if(produitOptional.isPresent()) {
            Produit produit = produitOptional.get();
            if(produit.getStatut() == 1) throw new ProduitException("un produit existe deja avec le nom "+
                    request.getNom());
            produit.setDescription(request.getDescription());
            produit.setStatut(1);
            produit.setDateCreation(now());
            produit.setDatSuppression(null);
            produitRepository.save(produit);
        }
        product.setDescription(request.getDescription());
        product.setNom(request.getNom().toUpperCase());
        product.setCodeProduit(CodeGenerator.generateCode(request.getNom(),request.getDescription()));
        product.setStatut(1);
        product.setDateCreation(now());
        product.setDatSuppression(null);
        return produitRepository.save(product);


    }

    public Produit update(String code, ProduitRequest request){
        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(code,1);
        if(produitOptional.isEmpty()) throw new ProduitException("aucun produit avec l'identifiant "+code);
        product = produitOptional.get();
        product.setNom(request.getNom().toUpperCase());
        product.setDescription(request.getDescription());

        return produitRepository.save(product);

    }

    public Produit read(String code){
        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(code,1);
        if(produitOptional.isEmpty()) throw  new ProduitException("aucun produit avec l'identifiant "+code);
        return produitOptional.get();
    }

    public List<Produit> all(){
        return produitRepository.findAllByStatut(1);
    }

    public List<ProduitReturnRequest> lister(){
        List<Object[]> results = produitRepository.findAllProduitAndCountModule();
        List<ProduitReturnRequest> produits = new ArrayList<>();

        for (Object[] result : results) {
            String codeproduit = (String) result[0];
            String nom = (String) result[1];
            String description = (String) result[2];
            int statut = ((Number) result[3]).intValue();
            int nbrModuleStandard = ((Number) result[4]).intValue();
            int nbrModuleAdditionnel = ((Number) result[5]).intValue();

            ProduitReturnRequest produit = new ProduitReturnRequest();
            produit.setCodeProduit(codeproduit);
            produit.setNom(nom.toUpperCase());
            produit.setStatut(statut);
            produit.setDescription(description);
            produit.setNbrModuleStandard(nbrModuleStandard);
            produit.setNbrModuleAdditionnel(nbrModuleAdditionnel);

            produits.add(produit);
        }

        return produits;
    }


    public List<Produit> allDeleted(){
        return produitRepository.findAllByStatut(2);
    }

    public Produit delete(String code){
        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(code,1);
        if(produitOptional.isEmpty()) throw  new ProduitException("aucun produit avec l'identifiant "+code);
        product = produitOptional.get();
        product.setStatut(2);
        product.setDatSuppression(now());

        return produitRepository.save(product);
    }
}
