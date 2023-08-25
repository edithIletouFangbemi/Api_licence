package com.example.Api_version.Services;

import com.example.Api_version.entities.Historique;
import com.example.Api_version.entities.Institution;
import com.example.Api_version.exceptions.InstitutionException;
import com.example.Api_version.repositories.HistoryRepository;
import com.example.Api_version.repositories.InstitutionRepository;
import com.example.Api_version.request.InstitutionRequest;
import com.example.Api_version.request.InstitutionReturnRequest;
import com.example.Api_version.request.Statistique;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@Transactional
public class InstitutionService {
    private InstitutionRepository institutionRepository;

    private HistoryRepository historyRepository;

    public InstitutionService(InstitutionRepository institutionRepository, HistoryRepository historyRepository) {
        this.institutionRepository = institutionRepository;
        this.historyRepository = historyRepository;
    }
    public Institution creer(InstitutionRequest request){
        Optional<Institution> institutionOptional = institutionRepository.findByNomInst(request.getNomInst());
        if (institutionOptional.isPresent()){
            Institution inst = institutionOptional.get();
            if(inst.getStatut() == 1) throw new InstitutionException("Une institution existe deja avec le nom: "+ inst.getNomInst());
            if(inst.getStatut() == 2){
                inst.setStatut(1);
                inst.setDateCreation(now());
                inst.setDateSuppression(null);
                institutionRepository.save(inst);
            }
        }
        Institution institution = new Institution();

        institution.setDateCreation(now());
        institution.setCodeInst(CodeGenerator.generateCode(request.getNomInst(), request.getAdresseInst()));
        institution.setNomInst(request.getNomInst());
        institution.setTypeArchitecture(request.getTypeArchitecture());
        institution.setStatut(1);
        institution.setAdresseInst(request.getAdresseInst());

        var historique = new Historique();
        historique.setAction("Création de l'institution "+institution.getNomInst());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur("");

        historyRepository.save(historique);
        return institutionRepository.save(institution);

    }

    public Institution update(String code, InstitutionRequest institution){
        Optional<Institution> institutionOptional = institutionRepository.findByCodeInst(code);
        if(institutionOptional.isEmpty() || institutionOptional.get().getStatut() == 2) throw new InstitutionException("aucune institution avec le code "+ code);

            Institution inst = institutionOptional.get();
            var valeur = inst.getNomInst();
            inst.setNomInst(institution.getNomInst());
            inst.setAdresseInst(institution.getAdresseInst());
            inst.setTypeArchitecture(institution.getTypeArchitecture());

        var historique = new Historique();
        historique.setAction("Modification de "+valeur + " en "+inst.getNomInst());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur("");

        historyRepository.save(historique);
            return institutionRepository.save(inst);



    }
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


            InstitutionReturnRequest request = new InstitutionReturnRequest();
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
            historique.setAuteur("");

            historyRepository.save(historique);


            institutions.add(request);
        }

        return institutions;
    }

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



    public Institution OneInstitution(String code){
        Optional<Institution> institutionOptional = institutionRepository.findById(code);

        if(institutionOptional.isEmpty() || institutionOptional.get().getStatut() == 2) throw new InstitutionException("aucune institution avec le code "+ code);
        Institution inst = institutionOptional.get();
        var historique = new Historique();
        historique.setAction("Consulter l'institution "+inst.getNomInst());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur("");

        historyRepository.save(historique);
        return inst;
    }

    public String supprimer(String code){
        Optional<Institution> institutionOptional = institutionRepository.findById(code);
        Institution inst = institutionOptional.get();
        if(institutionOptional.isEmpty() || inst.getStatut() == 2) throw new InstitutionException("aucune institution avec le code "+ code);
        inst.setStatut(2);
        var historique = new Historique();
        historique.setAction("Suppression de "+inst.getNomInst());
        historique.setStatut(1);
        historique.setDateCreation(LocalDateTime.now());
        historique.setAuteur("");

        historyRepository.save(historique);
        return inst.getNomInst()+" a été supprimée avec succès!";
    }
}
