package com.example.Api_version.Services;

import com.example.Api_version.entities.Historique;
import com.example.Api_version.entities.Institution;
import com.example.Api_version.exceptions.InstitutionException;
import com.example.Api_version.exceptions.UserException;
import com.example.Api_version.repositories.HistoryRepository;
import com.example.Api_version.repositories.InstitutionRepository;
import com.example.Api_version.request.InstitutionRequest;
import com.example.Api_version.request.InstitutionReturnRequest;
import com.example.Api_version.request.Statistique;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

/**
 * Service de gestion des institutions
 */
@Service
@Transactional
public class InstitutionService {
    private InstitutionRepository institutionRepository;

    private HistoryRepository historyRepository;

    private AuthenticationService authenticationService;

    public InstitutionService(InstitutionRepository institutionRepository, HistoryRepository historyRepository, AuthenticationService authenticationService) {
        this.institutionRepository = institutionRepository;
        this.historyRepository = historyRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * Création d'une institution
     * @param request
     * @return l'objet institution créé
     */
    @Transactional
    public Institution creer(InstitutionRequest request){
        Optional<Institution> institutionOptional = institutionRepository.findByNomInstIgnoreCaseAndStatut(request.getNomInst(), 1);
        Optional<Institution> optionalInstitutionByCode = institutionRepository.findByCodeInstAndStatut(request.getCodeInst(), 1);

        if(optionalInstitutionByCode.isPresent()) throw new InstitutionException("une institution existe deja avec le code "+request.getCodeInst(), HttpStatus.CONFLICT);
        if (institutionOptional.isPresent()){
            Institution inst = institutionOptional.get();
            if(inst.getStatut() == 1) throw new InstitutionException("Une institution existe deja avec le nom: "+ inst.getNomInst(),HttpStatus.CONFLICT);
            if(inst.getStatut() == 2){
                inst.setStatut(1);
                inst.setDateCreation(now());
                inst.setDateSuppression(null);
                institutionRepository.save(inst);
            }
        }

        Institution institution = new Institution();

        institution.setDateCreation(now());
        institution.setCodeInst(request.getCodeInst());
        institution.setNomInst(request.getNomInst());
        institution.setTypeArchitecture(request.getTypeArchitecture());
        institution.setStatut(1);
        institution.setAdresseInst(request.getAdresseInst());

        var historique = new Historique();
        historique.setAction("Création de l'institution "+institution.getNomInst());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur(authenticationService.getCurrentUsername());

        historyRepository.save(historique);
        return institutionRepository.save(institution);

    }

    /**
     * modifier une institution
     * @param id
     * @param institution
     * @return
     */
    @Transactional
    public Institution update(int id, InstitutionRequest institution){
        Optional<Institution> institutionOptional = institutionRepository.findByIdAndStatut(id,1);
        if(institutionOptional.isEmpty() || institutionOptional.get().getStatut() == 2) throw new InstitutionException("aucune institution avec l'identifiant "+ id, HttpStatus.NOT_FOUND);

            Institution inst = institutionOptional.get();
            var valeur = inst.getNomInst();
            inst.setCodeInst(institution.getCodeInst());
            inst.setNomInst(institution.getNomInst());
            inst.setAdresseInst(institution.getAdresseInst());
            inst.setTypeArchitecture(institution.getTypeArchitecture());

        var historique = new Historique();
        historique.setAction("Modification de l'institution "+valeur + " en "+inst.getNomInst());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur(authenticationService.getCurrentUsername());

        historyRepository.save(historique);
            return institutionRepository.save(inst);
    }

    /**
     * liste des institutions
     * @return
     */
    @Transactional
    public List<InstitutionReturnRequest> liste(){
        List<Object[]> results = institutionRepository.findAllByStatut(1);
        List<InstitutionReturnRequest> institutions = new ArrayList<>();
        for(Object[] result: results){
            String codeinst = (String) result[0];
            String nominst = (String) result[1];
            String adresseinst = (String) result[2];
            int statut = ((Number) result[3]).intValue();
            int nbragence = ((Number) result[4]).intValue();
            Date datecreation = (Date)result[5];
            String typeArchitecture = (String)result[6];
            int id = ((Number)result[7]).intValue();

            InstitutionReturnRequest request = new InstitutionReturnRequest();
            request.setId(id);
            request.setCodeInst(codeinst);
            request.setNomInst(nominst);
            request.setAdresseInst(adresseinst);
            request.setStatut(statut);
            request.setTypeArchitecture(typeArchitecture);
            request.setDateCreation(datecreation);
            request.setNbrAgence(nbragence);

            var historique = new Historique();
            historique.setAction("Consulter la liste des institutions ");
            historique.setStatut(1);
            historique.setDateCreation(LocalDateTime.now());
            historique.setAuteur(authenticationService.getCurrentUsername());

            historyRepository.save(historique);

            institutions.add(request);
        }

        return institutions;
    }

    /**
     * liste des Institutions avec leur nombre d'agence
     * @return
     */
    public List<Statistique> listeCountAgence(){
        List<Object[]> statistiqueList = institutionRepository.countAgence();
        List<Statistique> listeStat = new ArrayList<>();
        for(Object[] stat:statistiqueList ){
            String libelle = (String)stat[0];
            int nbr = ((Number)stat[1]).intValue();

            var statistique = new Statistique();
            statistique.setLibelle(libelle);
            statistique.setNombre(nbr);

            listeStat.add(statistique);
        }
        return listeStat;
    }


    /**
     * recupérer une institution
     * @param code
     * @return inst
     */
    public Institution OneInstitution(int code){
        Optional<Institution> institutionOptional = institutionRepository.findById(code);

        if(institutionOptional.isEmpty() || institutionOptional.get().getStatut() == 2) throw new InstitutionException("aucune institution avec l'identifiant "+ code, HttpStatus.NOT_FOUND);
        Institution inst = institutionOptional.get();
        var historique = new Historique();
        historique.setAction("Consulter l'institution "+inst.getNomInst());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur(authenticationService.getCurrentUsername());

        historyRepository.save(historique);
        return inst;
    }

    /**
     * suppression d'une instituion
     * @param code
     * @return
     */
    @Transactional
    public Institution supprimer(int code){
        Optional<Institution> institutionOptional = institutionRepository.findById(code);
        Institution inst = institutionOptional.get();
        if(institutionOptional.isEmpty() || inst.getStatut() == 2) throw new InstitutionException("aucune institution avec l'identifiant "+ code,HttpStatus.NOT_FOUND);
        inst.setStatut(2);
        var historique = new Historique();
        historique.setAction("Suppression de "+inst.getNomInst());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur(authenticationService.getCurrentUsername());

        historyRepository.save(historique);
        return institutionRepository.save(inst);
    }

    /**
     * liste des institutions avec les nombres de produit qu'ils ont
     * @return
     */
    public List<Statistique> countWithProduit(){
        List<Object[]> results = institutionRepository.countWithProduit();
        List<Statistique> listeRetour = new ArrayList<>();
        for(Object[] result: results){
            String institution = (String)result[0];
            int nombre = ((Number)result[1]).intValue();

            var stat = new Statistique();
            stat.setLibelle(institution);
            stat.setNombre(nombre);

            listeRetour.add(stat);
        }
        return  listeRetour;
    }

}
