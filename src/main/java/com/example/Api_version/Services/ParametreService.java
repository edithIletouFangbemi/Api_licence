package com.example.Api_version.Services;

import com.example.Api_version.entities.Parametre;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.ParametreRepository;
import com.example.Api_version.request.ParametreRequest;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.LocalTime.now;

@Service
public class ParametreService {
    private final ParametreRepository parametreRepository;
    private Parametre parametre;

    public ParametreService(ParametreRepository parametreRepository) {
        this.parametreRepository = parametreRepository;
    }

    public Parametre creer(ParametreRequest request){
        String description = "";
        description = String.join(",", request.getParametres());

        /*
        Optional<Parametre> parametreOptional = parametreRepository.findByLibelleAndDateDebut(
                CodeGenerator.libelleParametre(request.getParametres()),
                request.getDateDebut());
        if(parametreOptional.isPresent()){
            if(parametreOptional.get().getStatut() == 1) throw new ProduitException("Un tel parametre est deja en cours... ");
            parametre = parametreOptional.get();
            parametre.setDateDebut(request.getDateDebut());
            parametre.setStatut(1);
            parametreRepository.save(parametre);
        }*/

        Optional<Parametre> param = parametreRepository.findByStatut(1);
        if(param.isPresent()){
            parametre = param.get();
            parametre.setDateFin(LocalDateTime.now());
            parametre.setStatut(2);
            parametreRepository.save(parametre);
        }

            parametre = new Parametre();
            parametre.setDateDebut(request.getDateDebut());
            parametre.setDescription(description);
            parametre.setStatut(1);
            parametre.setLibelle(CodeGenerator.libelleParametre(request.getParametres()));
            parametre.setCodeParametre(CodeGenerator.codeParametre(parametre.getLibelle()));
            return parametreRepository.save(parametre);


    }

    public Parametre getOne(String codeParametre){
        Optional<Parametre> parametreOptional = parametreRepository.findByCodeParametreAndStatut(codeParametre,1);

        if(parametreOptional.isEmpty()) throw new ProduitException("Aucun parametre avec "+codeParametre);

        return parametreOptional.get();
    }

    public Parametre activeParam(){
        return parametreRepository.findByStatut(1).get();
    }

    public List<Parametre> lister(){
        return parametreRepository.findAllParametre();
    }

    public Parametre editer(String code, ParametreRequest request){
        Optional<Parametre> parametreOptional = parametreRepository.findByCodeParametreAndStatut(code,1);
        if(parametreOptional.isEmpty()) throw new ProduitException("Aucun parametre avec le code "+code);

        parametre = parametreOptional.get();
        String description = String.join(",", request.getParametres());
        parametre.setDescription(description);
        parametre.setLibelle(CodeGenerator.libelleParametre(request.getParametres()));
        parametre.setDateDebut(request.getDateDebut());
        return parametreRepository.save(parametre);
    }

    public Parametre supprimer(String code){
        Optional<Parametre> parametreOptional = parametreRepository.findByCodeParametreAndStatut(code,1);
        if(parametreOptional.isEmpty()) throw new ProduitException("Aucun parametre avec le code "+code);
        parametre = parametreOptional.get();
        parametre.setStatut(2);
        parametre.setDateFin(LocalDateTime.now());
        return parametreRepository.save(parametre);
    }
}
