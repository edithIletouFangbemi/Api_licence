package com.example.Api_version.Services;

import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.ContratPrdouitRepository;
import com.example.Api_version.repositories.ContratRepository;
import com.example.Api_version.repositories.DetailContratModuleRepository;
import com.example.Api_version.repositories.DetailContratRepository;
import com.example.Api_version.request.DetailContratRequest;
import com.example.Api_version.utils.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class IntermediaireService {
    private final DetailContratRepository detailContratRepository;

    private DetailContrat detail;

    private final DetailContratModuleRepository detailContratModuleRepository;

    private DetailContratModule detailContratModule;


    public DetailContrat creer(Agence agence, List<Module> modules){

        modules.forEach(mod->{
            detail = new DetailContrat();
        //    detail.setCodeDetailContrat(CodeGenerator.codeDetailContrat(agence.getNom()));
            detail.setAgence(agence);
            detail.setStatut(1);
            detail.setLibelle("Activation de module pour "+ agence.getNom());
            detail.setDate_debut(Date.from(now().atZone(ZoneId.systemDefault()).toInstant()));
            detail.setModule(mod);
            detail.setNbrPoste(0);
        });
        return detailContratRepository.save(detail);
    }
}
