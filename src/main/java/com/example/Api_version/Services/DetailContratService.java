package com.example.Api_version.Services;

import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import com.example.Api_version.exceptions.AgenceException;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.*;
import com.example.Api_version.request.DetailContratRequest;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
public class DetailContratService {
    private final DetailContratRepository detailContratRepository;
    private final AgenceService agenceService;
    private Agence agence;
    private Institution institution;
    private DetailContrat detail;
    private final ModuleService moduleService;
    private final Sous_ContratRepository sousContratRepository;
    private final ContratRepository contratRepository;

    public DetailContratService(DetailContratRepository detailContratRepository,
                                AgenceService agenceService, ModuleService moduleService, Sous_ContratRepository sousContratRepository, ContratRepository contratRepository) {
        this.detailContratRepository = detailContratRepository;
        this.agenceService = agenceService;
        this.moduleService = moduleService;
        this.sousContratRepository = sousContratRepository;
        this.contratRepository = contratRepository;
    }

    public DetailContrat creer(DetailContratRequest request){
        agence = agenceService.uneAgence(request.getIdAgence());
        Optional<Contrat_Institution> contratOptional = contratRepository.findByIdAndStatut(request.getIdContrat(), 1) ;

        if(agence == null) throw new AgenceException("Aucune agence avec l'identifiant "+request.getIdAgence(), HttpStatus.NOT_FOUND);
        if(contratOptional.isEmpty()) throw new AgenceException("Aucun contrat avec l'id "+request.getIdContrat(), HttpStatus.NOT_FOUND);

        Module module = moduleService.read(request.getIdModule());

        if(module == null) throw new AgenceException("Aucun module avec l'identifiant "+ request.getIdModule(), HttpStatus.NOT_FOUND);

        Optional<DetailContrat> detailContratOptional = detailContratRepository.findByAgenceAndModuleAndStatut(agence, module, 1);

        if(detailContratOptional.isPresent()){
            detail = detailContratOptional.get();

            if(detail.getTypeContratModule().equals("limite") && detail.getNbrPoste() >= 0) throw new AgenceException(module.getLibelleModule()+" est deja actif pour "+agence.getNom()+" vous pouvez le modifier ou la suspendre !!",HttpStatus.BAD_REQUEST);

            if(detail.getTypeContratModule().equals("illimite")) throw new AgenceException(module.getLibelleModule()+" est dejà activé de façon illimitée pour "+agence.getNom(), HttpStatus.BAD_REQUEST);
        }

        Optional<Sous_Contrat> sousContratOptional = sousContratRepository.findByIdAndContratAndStatutAndType(request.getIdSousContrat(), contratOptional.get(),1,0);
        if(sousContratOptional.isEmpty()) throw new AgenceException(" Aucun contrat avec l'identifiant "+request.getIdSousContrat(), HttpStatus.NOT_FOUND);
        institution = agence.getInstitution();

        if(!module.getProduit().equals(sousContratOptional.get().getProduit())) throw new AgenceException(module.getLibelleModule()+" n'appartient à aucun produit du contrat de "+ institution.getNomInst(), HttpStatus.NOT_FOUND);

        /*request.getModules().forEach(codeModule->{
            //var module = moduleService.read(codeModule);
            var module = moduleService.read(1);
            if(module == null) throw new ProduitException("aucun module avec le code "+codeModule);
            if(!produits.contains(module.getProduit())) throw new ProduitException("Le produit "+module.getProduit().getNom()+
                    " n'est pas activé pour l'institution "+ institution.getNomInst() +" Donc vous ne pouvez pas activer le module "+module.getLibelleModule());
            modules.add(module);
        });
        if(contrat == null) throw new ProduitException("Aucun contrat en cours pour l'institution "+ institution.getNomInst()+
                " de l'agence "+ agence.getNom());
        if(contrat.getTypeContrat().equals(TypeContrat.valueOf("AgenceIllimité_PosteLimité").toString())
                || contrat.getTypeContrat().equals(TypeContrat.valueOf("AgenceLimité_PosteLimité").toString())){
            agences = agenceService.ListeParInstitution(institution.getId());
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
            */

        detail = new DetailContrat();
        detail.setCodeDetailContrat(CodeGenerator.codeDetailContrat(agence.getNom(), module.getLibelleModule()));
        detail.setAgence(agence);
        detail.setStatut(1);
        detail.setLibelle("Activation du module "+module.getLibelleModule() +" pour "+ agence.getNom());
        detail.setDate_debut(Date.from(now().atZone(ZoneId.systemDefault()).toInstant()));
        detail.setModule(module);
        detail.setSousContrat(sousContratOptional.get());
        detail.setTypeContratModule(request.getTypeContratModule());
        if(detail.getCodeDetailContrat().equals("illimite")){
            detail.setNbrPoste(0);
        }
        if(detail.getCodeDetailContrat().equals("limite")){
            if(request.getNbrPoste() >= 0) throw new AgenceException("le nombre de poste pour l'activation du module "+detail.getModule().getLibelleModule()
                    +" à "+detail.getAgence().getNom()+" doit être supérieur à 0 ", HttpStatus.NOT_FOUND);
            detail.setNbrPoste(request.getNbrPoste());
        }
       /* modules.forEach(mod->{
            detailContratModule = new DetailContratModule();
            detailContratModule.setCodeModule(mod.getCodeModule());
            detailContratModule.setDateCreation(LocalDateTime.now());
            detailContratModule.setStatut(1);
            detailContratModule.setCodeDetail(detail.getCodeDetailContrat());
            detailContratModule.setCodeAgence(agence.getCodeAgence());
            detailContratModuleRepository.save(detailContratModule);
        });*/

        return detailContratRepository.save(detail);
    }

    public List<DetailContrat> liste(){
        return detailContratRepository.findAllByStatut(1);
    }

    public DetailContrat getOne(int id){
        Optional<DetailContrat> contratOptional = detailContratRepository.findByIdAndStatut(id,1);

        if(contratOptional.isEmpty()) throw new ProduitException(" aucun detail de contrat avec l'identifiant "+id);
        return contratOptional.get();
    }

    public DetailContrat update(int idDetailContrat, DetailContratRequest request){
        return null;
    }

    public List<DetailContrat> getAllByAgence(int idAgence){
        agence = agenceService.uneAgence(idAgence);
        if(agence == null) throw new AgenceException("Aucune agence avec l'identifiant "+idAgence, HttpStatus.NOT_FOUND);
        List<DetailContrat> detailContratList = detailContratRepository.findByAgenceAndStatut(agence,1);

        return detailContratList;
    }
}
