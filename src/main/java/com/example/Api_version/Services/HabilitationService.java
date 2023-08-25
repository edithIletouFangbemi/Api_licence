package com.example.Api_version.Services;

import com.example.Api_version.entities.Habilitation;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.HabilitationRepository;
import com.example.Api_version.request.HabilitationRequest;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
public class HabilitationService {
    private Habilitation habil;
    private HabilitationRepository habilitationRepository;

    public HabilitationService(HabilitationRepository habilitationRepository) {
        this.habilitationRepository = habilitationRepository;
    }

    public Habilitation creer(HabilitationRequest request){
        Optional<Habilitation> habilitation = habilitationRepository.findByLibelle(request.getLibelle());
        if(habilitation.isPresent()){
            habil = habilitation.get();
            if(habil.getStatut() == 1) throw new ProduitException("cette habilitation existe deja!!");
            habil.setStatut(1);
            habil.setDateSuppression(null);
            habil.setDateCreation(now());
            habilitationRepository.save(habil);
        }
        habil = new Habilitation();
        habil.setCodeHabilitation(CodeGenerator.codeHabilitation(request.getLibelle()));
        habil.setLibelle(request.getLibelle());
        habil.setStatut(1);
        habil.setDateCreation(now());

        return habilitationRepository.save(habil);

    }

    public Habilitation one(String code){
        Optional<Habilitation> habilitationOptional = habilitationRepository.findByCodeHabilitationAndStatut(code,1);

        if(habilitationOptional.isEmpty()) throw new ProduitException("Aucune habilitation avec le code "+code);
        return habilitationOptional.get();
    }

    public List<Habilitation> liste(){
        return habilitationRepository.findAllByStatut(1);
    }

    public Habilitation update(String code,HabilitationRequest request){
        Optional<Habilitation> habilitationOptional = habilitationRepository.findByCodeHabilitationAndStatut(code, 1);
        if(habilitationOptional.isEmpty()) throw new ProduitException("aucune habilitaton avec le code "+code);
        habil = habilitationOptional.get();
        habil.setLibelle(request.getLibelle());

        return habilitationRepository.save(habil);

    }

    public String delete(String code){
        Optional<Habilitation> habilitationOptional = habilitationRepository.findByCodeHabilitationAndStatut(code, 1);
        if(habilitationOptional.isEmpty()) throw new ProduitException("aucune habilitaton avec le code "+code);
        habil = habilitationOptional.get();
        habil.setStatut(2);

        habilitationRepository.save(habil);

        return "suppression fait avec succès";
    }

    public String activer(String code){
        Optional<Habilitation> habilitationOptional = habilitationRepository.findByCodeHabilitationAndStatut(code, 2);
        if(habilitationOptional.isEmpty()) throw new ProduitException("aucune habilitaton avec le code "+code);
        habil = habilitationOptional.get();
        habil.setStatut(1);

        habilitationRepository.save(habil);

        return "Activée avec succès";
    }
}
