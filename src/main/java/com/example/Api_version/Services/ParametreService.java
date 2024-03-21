package com.example.Api_version.Services;

import com.example.Api_version.entities.Parametre;
import com.example.Api_version.exceptions.AgenceException;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.ParametreRepository;
import com.example.Api_version.request.ParametreRequest;
import com.example.Api_version.utils.CodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
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
        System.out.println(request);
        if(request.getDateDebut().after(request.getDateFin())) throw new AgenceException("la date de debut doit être inférieure à la date fin", HttpStatus.BAD_REQUEST);
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

       /* Optional<Parametre> param = parametreRepository.findByStatut(1);
        if(param.isPresent()){
            parametre = param.get();
            if(parametre.getDateFin() != null){
                if(parametre.getDateFin().isBefore(request.getDateDebut().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime())){

                    parametre.setDateFin(request.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minusDays(1));
                    parametre.setStatut(2);
                }
                if(parametre.getDateFin().isAfter(request.getDateDebut().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime())){
                   // request.setDateDebut(Date.from(parametre.getDateFin()));
                    parametre.setDateFin(parametre.getDateFin().minusDays(1));

                }
            }
            parametre.setDateFin(request.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minusDays(1));
            parametre.setStatut(2);
            parametreRepository.save(parametre);
        } */

            Long nbrCount = parametreRepository.countNbrParametreOverlapping(request.getDateDebut(),request.getDateFin());

            if(nbrCount > 0 ) throw new AgenceException("Les dates définies chevauchent les dates des parametres en cours !!", HttpStatus.BAD_REQUEST);

            parametre = new Parametre();
            parametre.setDateDebut(request.getDateDebut());
            if(request.getDateFin() != null){
                parametre.setDateFin(request.getDateFin());
            }
            parametre.setDescription(description);
            parametre.setStatut(1);
            parametre.setLibelle(CodeGenerator.libelleParametre(request.getParametres()));
            parametre.setCodeParametre(CodeGenerator.codeParametre(parametre.getLibelle()));
            return parametreRepository.save(parametre);

    }

    public Parametre getOne(int id){
        Optional<Parametre> parametreOptional = parametreRepository.findByIdAndStatut(id,1);

        if(parametreOptional.isEmpty()) throw new ProduitException("Aucun parametre avec l'identifiant "+id);

        return parametreOptional.get();
    }

    public Parametre activeParam(){
        return parametreRepository.findByStatut(1).get();
    }

    public Parametre getParamCheckingWithDate(Date dateForCheck){
        return parametreRepository.getParametreByDate(dateForCheck).get();
    }

    public List<Parametre> lister(){
        return parametreRepository.findAllParametre();
    }

    public Parametre editer(int id, ParametreRequest request){
        Optional<Parametre> parametreOptional = parametreRepository.findByIdAndStatut(id,1);
        if(parametreOptional.isEmpty()) throw new ProduitException("Aucun parametre avec l'identifiant "+id);
        parametre = parametreOptional.get();

        Long nbrCount = parametreRepository.countNbrParametreOverlappingAndCheckId(request.getDateDebut(),request.getDateFin(), parametre.getId());
        System.out.println(parametre.getId());
        System.out.println(nbrCount);
        if(nbrCount > 0 ) throw new AgenceException("Les dates définies chevauchent les dates des parametres en cours !!", HttpStatus.BAD_REQUEST);

        parametre = parametreOptional.get();
        String description = String.join(",", request.getParametres());
        parametre.setDescription(description);
        parametre.setLibelle(CodeGenerator.libelleParametre(request.getParametres()));
        parametre.setDateDebut(request.getDateDebut());
        return parametreRepository.save(parametre);
    }

    public Parametre supprimer(int id, Date dateFin){
        System.out.println();
        Optional<Parametre> parametreOptional = parametreRepository.findByIdAndStatut(id,1);
        if(parametreOptional.isEmpty()) throw new ProduitException("Aucun parametre avec l'identifiant"+id);
        parametre = parametreOptional.get();
        parametre.setStatut(2);
        parametre.setDateFin(dateFin);
        return parametreRepository.save(parametre);
    }
}
