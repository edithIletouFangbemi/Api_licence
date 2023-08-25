package com.example.Api_version.Services;

import com.example.Api_version.config.AESCryptor;
import com.example.Api_version.entities.Institution;
import com.example.Api_version.entities.Licence;
import com.example.Api_version.entities.Module;
import com.example.Api_version.entities.Poste;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.LicenceRepository;
import com.example.Api_version.repositories.ModuleRepository;
import com.example.Api_version.repositories.PosteRepository;
import com.example.Api_version.request.CheckReturn;
import com.example.Api_version.request.LicenceReturnRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class CheckExpiration {
    private final LicenceService licenceService;
    private final PosteRepository posteRepository;

    private final LicenceRepository licenceRepository;

    private final ModuleRepository moduleRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    String returnKey = "#@£%&é'(-è_çà)='?./§!:;,<*µ¤+=}^9";

    private CheckReturn reponse = new CheckReturn();

    public CheckReturn checkExpiration(LicenceReturnRequest request) throws Exception {
          reponse = licenceService.checkLicence(request);
          var response = new CheckReturn();

        Optional<Module> moduleOptional = moduleRepository.findByCodeModuleAndStatut(AESCryptor.decrypt(request.getModule(),returnKey), 1);

        if(moduleOptional.isEmpty()) throw new ProduitException("aucun module avec ce code ");

        var module = new Module();
        module = moduleOptional.get();

          if(reponse.getMessage()== "1"){
           var poste = new Poste();

           Optional<Poste> posteOptional = posteRepository.findByCodePosteAndStatut(AESCryptor.decrypt(request.getCodePoste(), returnKey),1);



           if(posteOptional.isEmpty()) throw new ProduitException("aucun poste n'a cette licence");

           poste = posteOptional.get();

           var licence = new Licence();

          Optional<Licence> licenceOptional = licenceRepository.findByPosteAndModule(poste, module);

          if(licenceOptional.isEmpty()) throw new ProduitException("aucune licence pour ce poste avec ce module !");

          licence = licenceOptional.get();

              try {
                  if(passwordEncoder.matches(AESCryptor.decrypt(request.getCodeLicence(), returnKey), licence.getLibelle())){
                      LocalDateTime dateExpiration = licence.getDateExpiration();
                      LocalDateTime dateActuelle = now();
                      long daysRemaining = Duration.between(dateActuelle, dateExpiration).toDays();
                      if(daysRemaining <= 3){
                          if(daysRemaining == 1){
                            response.setMessage("la licence du module "+ module.getLibelleModule() + " expire aujoourd'hui pour votre poste ");
                          }
                          if(daysRemaining == 0){
                              response.setMessage("la licence du module "+ module.getLibelleModule() + " a expiré pour votre poste ");

                              licence.setStatut(2);

                              licenceRepository.save(licence);

                          }
                      }
                      response.setModule(module.getCodeModule());


                  }
              } catch (Exception e) {
                  throw new RuntimeException(e);
              }



          } else if (reponse.getMessage()== "2") {
              response.setMessage("la licence du module"+module.getLibelleModule()+" pour votre poste a expiré");
              response.setModule(module.getCodeModule());
          }

        return response;
    }

}
