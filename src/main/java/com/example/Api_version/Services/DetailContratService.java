package com.example.Api_version.Services;

import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.ContratPrdouitRepository;
import com.example.Api_version.repositories.DetailContratModuleRepository;
import com.example.Api_version.repositories.DetailContratRepository;
import com.example.Api_version.request.DetailContratRequest;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
public class DetailContratService {
    private final DetailContratRepository detailContratRepository;
    private final AgenceService agenceService;
    private final ContratPrdouitRepository repository;
    private final ContratService contratService;
    private Agence agence;
    private Contrat_Institution contrat;
    private Institution institution;
    private List<Agence> agences = new ArrayList<>();
    private DetailContrat detail;
    private final ModuleService moduleService;
    private final DetailContratModuleRepository detailContratModuleRepository;

    private DetailContratModule detailContratModule;
    private final ProduitService produitService;

    private List<Produit> produits = new ArrayList<>();
    private int somme;
    private int nbr;
    private Produit produit;
    private List<Module> modules = new ArrayList<>();

    public DetailContratService(DetailContratRepository detailContratRepository,
                                AgenceService agenceService, ContratService contratService, ModuleService moduleService, DetailContratModuleRepository detailContratModuleRepository, ContratPrdouitRepository repository, ProduitService produitService) {
        this.detailContratRepository = detailContratRepository;
        this.agenceService = agenceService;
        this.contratService = contratService;
        this.moduleService = moduleService;
        this.detailContratModuleRepository = detailContratModuleRepository;
        this.repository = repository;
        this.produitService = produitService;
    }

    public DetailContrat creer(DetailContratRequest request){
        agence = agenceService.uneAgence(request.getAgence());
        if(agence == null) throw new ProduitException("Aucune agence avec le code "+request.getAgence());

        institution = agence.getInstitution();
        contrat = contratService.getByInstitution(institution);

        repository.findAllByContratInstitutionAndStatut(contrat.getCodeContrat(), 1)
                        .forEach(contratProduit -> {
                            produits.add(produitService.read(
                                    contratProduit.getProduit()
                            ));
                        });

        request.getModules().forEach(codeModule->{
            var module = moduleService.read(codeModule);
            if(module == null) throw new ProduitException("aucun module avec le code "+codeModule);
            if(!produits.contains(module.getProduit())) throw new ProduitException("Le produit "+module.getProduit().getNom()+
                    " n'est pas activé pour l'institution "+ institution.getNomInst() +" Donc vous ne pouvez pas activer le module "+module.getLibelleModule());
            modules.add(module);
        });
        if(contrat == null) throw new ProduitException("Aucun contrat en cours pour l'institution "+ institution.getNomInst()+
                " de l'agence "+ agence.getNom());
        if(contrat.getTypeContrat().equals(TypeContrat.valueOf("AgenceIllimité_PosteLimité").toString())
                || contrat.getTypeContrat().equals(TypeContrat.valueOf("AgenceLimité_PosteLimité").toString())){
            agences = agenceService.ListeParInstitution(institution.getCodeInst());
            agences.forEach(agence1 -> {
                Optional<DetailContrat> detailContratOptional =detailContratRepository.findByAgenceAndStatut(agence1,1);
                if(detailContratOptional.isPresent()){
                    detail = detailContratOptional.get();
                    somme += detail.getNbrPoste();
                }
            });
            if(somme > contrat.getNbrPosteTotal()) throw new ProduitException("le nombre de Poste pour le contrat de cette " +
                    "institution es deja atteint et c'est "+contrat.getNbrPosteTotal());
            if(somme < contrat.getNbrPosteTotal()){
                nbr = contrat.getNbrPosteTotal() - somme;
                if((somme + request.getNbrPoste()) > contrat.getNbrPosteTotal()) throw new
                        ProduitException("Le nombre de place restant est insuffisant, il ne reste que "+ nbr);
            }

        }

        detail = new DetailContrat();
        detail.setCodeDetailContrat(CodeGenerator.codeDetailContrat(agence.getNom()));
        detail.setAgence(agence);
        detail.setStatut(1);
        detail.setLibelle("Activation de module pour "+ agence.getNom());
        detail.setDate_debut(Date.from(now().atZone(ZoneId.systemDefault()).toInstant()));
        detail.setNbrPoste(0);
        modules.forEach(mod->{
            detailContratModule = new DetailContratModule();
            detailContratModule.setCodeModule(mod.getCodeModule());
            detailContratModule.setDateCreation(LocalDateTime.now());
            detailContratModule.setStatut(1);
            detailContratModule.setCodeDetail(detail.getCodeDetailContrat());
            detailContratModule.setCodeAgence(agence.getCodeAgence());
            detailContratModuleRepository.save(detailContratModule);
        });
        return detailContratRepository.save(detail);

    }

    public List<DetailContrat> liste(){
        return detailContratRepository.findAllByStatut(1);
    }

    public DetailContrat getOne(String code){
        Optional<DetailContrat> contratOptional = detailContratRepository.findByCodeDetailContratAndStatut(code,1);

        if(contratOptional.isEmpty()) throw new ProduitException(" aucun detail de contrat avec le code "+code);
        return contratOptional.get();
    }
}
