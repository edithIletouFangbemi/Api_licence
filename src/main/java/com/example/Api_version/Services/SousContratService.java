package com.example.Api_version.Services;

import com.example.Api_version.entities.Contrat_Institution;
import com.example.Api_version.entities.DetailContrat;
import com.example.Api_version.entities.Produit;
import com.example.Api_version.entities.Sous_Contrat;
import com.example.Api_version.exceptions.AgenceException;
import com.example.Api_version.repositories.ContratRepository;
import com.example.Api_version.repositories.ProduitRepository;
import com.example.Api_version.repositories.Sous_ContratRepository;
import com.example.Api_version.request.Sous_ContratRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SousContratService {
    private final Sous_ContratRepository sousContratRepository;
    private final DetailContratService detailContratService;
    private final ContratRepository contratRepository;
    private final ProduitRepository produitRepository;
    private Contrat_Institution contratInstitution;

    public SousContratService(Sous_ContratRepository sousContratRepository, DetailContratService detailContratService, ContratRepository contratRepository, ProduitRepository produitRepository) {
        this.sousContratRepository = sousContratRepository;
        this.detailContratService = detailContratService;
        this.contratRepository = contratRepository;
        this.produitRepository = produitRepository;
    }


    @Transactional
    public Sous_Contrat create(Sous_ContratRequest request){
       Optional<Contrat_Institution> contratOptional = contratRepository.findByIdAndStatut(request.getIdContrat(), 1);
       Optional<Produit> produitOptional = produitRepository.findByIdAndStatut(request.getIdProduit(), 1);
       if(contratOptional.isEmpty()) throw new AgenceException("aucun contrat avec l'identifiant "+request.getIdContrat(), HttpStatus.NOT_FOUND);
       if(produitOptional.isEmpty()) throw new AgenceException(" aucun produit avec l'identifiant "+request.getIdProduit(), HttpStatus.NOT_FOUND);
       if(!(request.getTypeContrat().equals("AgenceLimite_PosteIllimite")
               || request.getTypeContrat().equals("AgenceLimite_PosteLimite") || request.getTypeContrat().equals("AgenceIllimite_PosteIllimite")))
           throw new AgenceException("type de contrat choisi est incorrect ", HttpStatus.BAD_REQUEST);
       contratInstitution = contratOptional.get();

       Sous_Contrat sousContrat = new Sous_Contrat();
       sousContrat.setContrat(contratInstitution);
       sousContrat.setDateCreation(LocalDateTime.now());
       sousContrat.setDateDebut(request.getDateDebut());
       sousContrat.setTypeContrat(request.getTypeContrat());
       if(request.getDateFin() != null){
           sousContrat.setDateFin(request.getDateFin());
       }

       if(request.getTypeContrat().equals("AgenceLimite_PosteIllimite") || request.getTypeContrat().equals("AgenceLimite_PosteLimite" )){
           if(request.getNbrAgence() == 0) throw new AgenceException(" le nombre d'agence doit être fourni ", HttpStatus.BAD_REQUEST);
           sousContrat.setNbrAgence(request.getNbrAgence());
           if(request.getTypeContrat().equals("AgenceLimite_PosteIllimite")){
               sousContrat.setNbrPosteTotal(0);
           }
           if(request.getTypeContrat().equals("AgenceLimite_PosteLimite" )){
               if(request.getNbrPosteTotal() == 0 ) throw new AgenceException("le nombre de poste doit être superieur à 0 ", HttpStatus.BAD_REQUEST);
               sousContrat.setNbrPosteTotal(request.getNbrPosteTotal());
           }
       }

       if(request.getTypeContrat().equals("AgenceIllimite_PosteIllimite")){
           sousContrat.setNbrAgence(0);
           sousContrat.setNbrPosteTotal(0);
       }
       if(request.getDateFin() != null){
           sousContrat.setDateFin(request.getDateFin());
       }

       sousContrat.setProduit(produitOptional.get());
       sousContrat.setType(0);
       sousContrat.setStatut(1);

       sousContrat = sousContratRepository.save(sousContrat);

        Sous_Contrat finalSousContrat = sousContrat;
        request.getDetailContratRequests().forEach(detail->{
           detail.setIdSousContrat(finalSousContrat.getId());
           detail.setIdContrat(contratInstitution.getId());

           detailContratService.creer(detail);
       });



    return sousContrat;
    }

    public List<Sous_Contrat> getAllByContrat(Contrat_Institution contrat){
        return sousContratRepository.findAllByContratAndStatut(contrat, 1);
    }

    public List<DetailContrat> getAllByAgence(int idAgence){
        return detailContratService.getAllByAgence(idAgence);
    }


}
