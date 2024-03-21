package com.example.Api_version.Services;

import com.example.Api_version.entities.Historique;
import com.example.Api_version.entities.Produit;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.HistoryRepository;
import com.example.Api_version.repositories.ProduitRepository;
import com.example.Api_version.request.ProduitRequest;
import com.example.Api_version.request.ProduitReturnRequest;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@Transactional
public class ProduitService {
    private ProduitRepository produitRepository;
    private Produit product = new Produit();
    private HistoryRepository historyRepository;
    private AuthenticationService authenticationService;

    public ProduitService(ProduitRepository produitRepository, HistoryRepository historyRepository, AuthenticationService authenticationService) {
        this.produitRepository = produitRepository;
        this.historyRepository = historyRepository;
        this.authenticationService = authenticationService;
    }

    public Produit creer(ProduitRequest request){
        Optional<Produit> produitOptional = produitRepository.findByNomIgnoreCase(request.getNom());

        Optional<Produit> produitOptional1 = produitRepository.findByCodeProduitIgnoreCase(request.getCodeProduit());

        if(produitOptional1.isPresent()){
            Produit produit2 = produitOptional1.get();
            if(produit2.getStatut()== 1) throw new ProduitException("un produit existe deja avec le code "+produit2.getCodeProduit());
        }

        if(produitOptional.isPresent()) {
            Produit produit = produitOptional.get();
            if(produit.getStatut() == 1) throw new ProduitException("un produit existe deja avec le nom "+
                    request.getNom());
            produit.setDescription(request.getDescription());
            produit.setCodeProduit(request.getCodeProduit());
            produit.setStatut(1);
            produit.setDateCreation(now());
            produit.setDatSuppression(null);

            var historique = new Historique();
            historique.setAction("Création du produit "+produit.getNom());
            historique.setStatut(1);
            historique.setDateCreation(LocalDateTime.now());
            historique.setAuteur(authenticationService.getCurrentUsername());

            historyRepository.save(historique);

            produitRepository.save(produit);
        }
        product = new Produit();
        product.setDescription(request.getDescription());
        product.setCodeProduit(request.getCodeProduit());
        product.setNom(request.getNom().toUpperCase());
        product.setCodeProduit(request.getCodeProduit());
        product.setStatut(1);
        product.setDateCreation(now());
        product.setDatSuppression(null);

        var historique = new Historique();
        historique.setAction("Création du produit "+product.getNom());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur(authenticationService.getCurrentUsername());

        historyRepository.save(historique);

        return produitRepository.save(product);

    }

    public Produit update(int id, ProduitRequest request){

        Optional<Produit> produitOptional1 = produitRepository.findByCodeProduitIgnoreCase(request.getCodeProduit());

        if(produitOptional1.isPresent()){
            Produit produit2 = produitOptional1.get();
            if(produit2.getStatut()== 1) throw new ProduitException("un produit existe deja avec le code "+produit2.getCodeProduit());
        }

        Optional<Produit> produitOptional = produitRepository.findByIdAndStatut(id,1);
        if(produitOptional.isEmpty()) throw new ProduitException("aucun produit avec l'identifiant "+id);

        var historique = new Historique();
        historique.setAction("mise à jour du produit "+product.getNom()+" en "+request.getNom());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur(authenticationService.getCurrentUsername());

        product = produitOptional.get();
        product.setNom(request.getNom().toUpperCase());
        product.setCodeProduit(request.getCodeProduit());
        product.setDescription(request.getDescription());

        historyRepository.save(historique);

        return produitRepository.save(product);
    }

    public Produit read(int id){
        Optional<Produit> produitOptional = produitRepository.findByIdAndStatut(id,1);
        if(produitOptional.isEmpty()) throw  new ProduitException("aucun produit avec l'identifiant "+id);
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
            int id = ((Number) result[6]).intValue();

            ProduitReturnRequest produit = new ProduitReturnRequest();
            produit.setId(id);
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

    public Produit delete(int id){
        Optional<Produit> produitOptional = produitRepository.findByIdAndStatut(id,1);
        if(produitOptional.isEmpty()) throw  new ProduitException("aucun produit avec l'identifiant "+id);
        product = produitOptional.get();
        product.setStatut(2);
        product.setDatSuppression(now());

        return produitRepository.save(product);
    }
}
