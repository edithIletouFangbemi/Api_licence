package com.example.Api_version.Services;

import com.example.Api_version.entities.ParametreDeVieLicence;
import com.example.Api_version.exceptions.AgenceException;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.ParametreDeVieRepository;
import com.example.Api_version.request.ParametreDeVieRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ParametreDevieService {
    private final ParametreDeVieRepository parametreDeVieRepository;

    public ParametreDeVieLicence creer(ParametreDeVieRequest request){
        Optional<ParametreDeVieLicence> parametreDeVieLicenceOptional = parametreDeVieRepository.findByStatutAndTypeLicence(1, request.getTypeLicence());

        if(parametreDeVieLicenceOptional.isPresent()) throw new AgenceException("Un parametre est actif actuellement desactiver le d'abord !!", HttpStatus.ALREADY_REPORTED);

        var parametre = new ParametreDeVieLicence();

        parametre.setTypeParametre(request.getTypeParametre());
        parametre.setQuantite(request.getQuantite());
        parametre.setDateCreation(now());
        parametre.setTypeLicence(request.getTypeLicence());
        parametre.setStatut(1);

        return parametreDeVieRepository.save(parametre);
    }

    public ParametreDeVieLicence read(int id){
        Optional<ParametreDeVieLicence> parametreDeVieLicenceOptional = parametreDeVieRepository.findByIdAndStatut(id, 1);
        if(parametreDeVieLicenceOptional.isEmpty()) throw new AgenceException("aucun parametre avec l'identifiant "+ id, HttpStatus.NOT_FOUND);
        return parametreDeVieLicenceOptional.get();
    }

    public ParametreDeVieLicence update(int id, ParametreDeVieRequest request){
        Optional<ParametreDeVieLicence> parametreDeVieLicenceOptional = parametreDeVieRepository.findByIdAndStatut(id, 1);
        if(parametreDeVieLicenceOptional.isEmpty()) throw new ProduitException("aucun parametre avec l'identifiant "+ id);
        var parametre = new ParametreDeVieLicence();
        parametre = parametreDeVieLicenceOptional.get();
        parametre.setTypeParametre(request.getTypeParametre());
        parametre.setQuantite(request.getQuantite());
        parametre.setTypeLicence(request.getTypeLicence());

        return parametreDeVieRepository.save(parametre);
    }

    public List<ParametreDeVieLicence> getAll(){
        Sort sortByAttributAsc = Sort.by(Sort.Direction.ASC, "statut");

        /*
        List<Object[]> results = parametreDeVieRepository.findAllParams();
        if(results.isEmpty()) throw new ProduitException("liste Vide !!");
        List<ParametreDeVieLicence> listeParametre = new ArrayList<>();
        for(Object[] result: results){
            int id = ((Number)result[0]).intValue();
        }*/

        return parametreDeVieRepository.findAll(sortByAttributAsc);
    }

    public ParametreDeVieLicence désactiver(int id){
        Optional<ParametreDeVieLicence> parametreDeVieLicenceOptional = parametreDeVieRepository.findByIdAndStatut(id, 1);
        if(parametreDeVieLicenceOptional.isEmpty()) throw new AgenceException("aucun parametre avec l'identifiant "+ id,HttpStatus.NOT_FOUND);
        var parametre = new ParametreDeVieLicence();
        parametre = parametreDeVieLicenceOptional.get();
        parametre.setDateFin(now());
        parametre.setStatut(2);
        return parametreDeVieRepository.save(parametre);
    }

    public ParametreDeVieLicence activer(int id){
        Optional<ParametreDeVieLicence> parametreDeVieLicenceOptional = parametreDeVieRepository.findByIdAndStatut(id, 2);
        if(parametreDeVieLicenceOptional.isEmpty()) throw new ProduitException("aucun parametre avec l'identifiant "+ id);
        var parametre = new ParametreDeVieLicence();
        parametre = parametreDeVieLicenceOptional.get();
        parametre.setDateFin(now());
        parametre.setStatut(1);
        return parametreDeVieRepository.save(parametre);
    }
}
