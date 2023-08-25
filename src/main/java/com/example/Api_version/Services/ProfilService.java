package com.example.Api_version.Services;

import com.example.Api_version.entities.Habilitation;
import com.example.Api_version.entities.Profil;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.HabilitationRepository;
import com.example.Api_version.repositories.ProfilRepository;
import com.example.Api_version.request.ProfilRequest;
import com.example.Api_version.request.ProfilReturnRequest;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProfilService {
    private ProfilRepository profilRepository;
    private HabilitationRepository habilitationRepository;
    private Profil profil;
    private List<Habilitation> habilitations;

    public ProfilService(ProfilRepository profilRepository,
                         HabilitationRepository habilitationRepository) {
        this.profilRepository = profilRepository;
        this.habilitationRepository = habilitationRepository;
    }

    public Profil creer(ProfilRequest request){
        Optional<Profil> profilOptional = profilRepository.findByLibelle(request.getLibelle());
        if (profilOptional.isPresent() && profilOptional.get().getStatut()==1) throw new ProduitException("ce profil existe deja!!");
        habilitations = new ArrayList<>();
        request.getHabilitations().forEach(habil->{
            Optional<Habilitation> habilitationOptional = habilitationRepository.findByCodeHabilitationAndStatut(habil,1);
            if(habilitationOptional.isEmpty()) throw new ProduitException("aucune habilitation avec le code "+ habil);
            habilitations.add(habilitationOptional.get());
        });
        profil = new Profil();
        profil.setHabilitations(habilitations);
        profil.setLibelle(request.getLibelle());
        profil.setStatut(1);
        profil.setCodeProfil(CodeGenerator.codeProfil(request.getLibelle()));

        return profilRepository.save(profil);

    }

    public Profil update(String code, ProfilRequest request){
        Optional<Profil> profilOptional = profilRepository.findByCodeProfilAndStatut(code,1);
        if (profilOptional.isEmpty()) throw new ProduitException("ce profil n'existe pas!!");

        profil = profilOptional.get();
        request.getHabilitations().forEach(habil->{
            Optional<Habilitation> habilitationOptional = habilitationRepository.findByCodeHabilitationAndStatut(habil,1);
            if(habilitationOptional.isEmpty()) throw new ProduitException("aucune habilitation avec le code "+ habil);
            habilitations.add(habilitationOptional.get());
        });

        profil.setHabilitations(habilitations);
        profil.setLibelle(request.getLibelle());
        profil.setCodeProfil(CodeGenerator.codeProfil(request.getLibelle()));

        return profilRepository.save(profil);
    }
    public List<Profil> liste(){
        return profilRepository.findAllByStatut(1);
    }

    public List<ProfilReturnRequest> lister(){
        List<Object[]> results = profilRepository.AllProfil();
        if(results.isEmpty()) throw new ProduitException("liste vide");
        List<ProfilReturnRequest> liste = new ArrayList<>();
        for(Object[] result: results){
            String codeProfil = (String) result[0];
            String libelle = (String) result[1];
            int statut = ((Number) result[2]).intValue();
            int nbrHabilitation = ((Number) result[3]).intValue();

            ProfilReturnRequest request = new ProfilReturnRequest();

            request.setNbrHabilitation(nbrHabilitation);
            request.setCodeProfil(codeProfil);
            request.setStatut(statut);
            request.setLibelle(libelle);
            liste.add(request);
        }
        return liste;
    }

    public String delete(String code){
        Optional<Profil> profilOptional = profilRepository.findByCodeProfilAndStatut(code,1);
        if (profilOptional.isEmpty()) throw new ProduitException("ce profil n'existe pas!!");

        profil = profilOptional.get();
        profil.setStatut(2);
        profilRepository.save(profil);

        return "desactivé avec succès!!";
    }

    public String activer(String code){
        Optional<Profil> profilOptional = profilRepository.findByCodeProfilAndStatut(code,2);
        if (profilOptional.isEmpty()) throw new ProduitException("ce profil n'existe pas!!");

        profil = profilOptional.get();
        profil.setStatut(1);
        profilRepository.save(profil);

        return "activé avec succès!!";
    }


}
