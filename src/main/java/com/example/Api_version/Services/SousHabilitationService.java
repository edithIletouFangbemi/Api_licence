package com.example.Api_version.Services;

import com.example.Api_version.entities.Habilitation;
import com.example.Api_version.entities.SousHabilitation;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.HabilitationRepository;
import com.example.Api_version.repositories.SousHabilitationRepository;
import com.example.Api_version.request.SousHabilitationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SousHabilitationService {
    private final SousHabilitationRepository sousHabilitationRepository;
    private final HabilitationRepository habilitationRepository;

    public SousHabilitation creer(SousHabilitationRequest request){
        Optional<Habilitation> habilitationOptional = habilitationRepository.findByCodeHabilitationAndStatut(request.getCodeHabilitation(), 1);
        var habilitation = new Habilitation();
        habilitation = habilitationOptional.get();
       if(habilitationOptional.isEmpty()) throw new ProduitException("Aucune habilitation avec le code "+request.getCodeHabilitation());

        Optional<SousHabilitation> sousHabilitationOptional = sousHabilitationRepository.findByLibelleAndHabilitation(request.getLibelle(), habilitation);
        var sousHabilitation = new SousHabilitation();
        if(sousHabilitationOptional.isPresent() ){
            sousHabilitation = sousHabilitationOptional.get();
            if(sousHabilitation.getStatut() == 1)
                throw new ProduitException(habilitation.getLibelle()+" a deja une sous habilitation avec le libelle "+sousHabilitation.getLibelle());
            else{
                if(sousHabilitation.getStatut() == 2){
                    sousHabilitation.setStatut(1);
                    sousHabilitationRepository.save(sousHabilitation);
                }
            }
        }


        sousHabilitation = new SousHabilitation();

        sousHabilitation.setHabilitation(habilitation);
        sousHabilitation.setLibelle(request.getLibelle());
        sousHabilitation.setStatut(1);
        return sousHabilitationRepository.save(sousHabilitation);
    }

    public List<SousHabilitation> listeSousHabilitation(String codeHabilitation){
        Optional<Habilitation> habilitationOptional = habilitationRepository.findByCodeHabilitationAndStatut(codeHabilitation, 1);
        var habilitation = habilitationOptional.get();
        if(habilitationOptional.isEmpty()) throw new ProduitException("Aucune habilitation avec le code "+ codeHabilitation);
        return sousHabilitationRepository.findAllByHabilitationAndStatut(habilitation,1);
    }

}
