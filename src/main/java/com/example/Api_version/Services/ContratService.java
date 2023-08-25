package com.example.Api_version.Services;

import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.*;
import com.example.Api_version.request.*;
import com.example.Api_version.utils.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ContratService {

    private final ContratRepository contratRepository;
    private final InstitutionRepository institutionRepository;
    private final ProduitRepository produitRepository;

    private final ContratPrdouitRepository contratPrdouitRepository;

    private final InstitutionService institutionService;
    private final ContratPrdouitRepository contratProduitRepository;
    private final ModuleRepository moduleRepository;
    private Institution institution;
    private List<Institution> listeInstitution;
    private List<Produit> listeProduit;
    private List<Module> modules;
    private final AgenceService agenceService;
    private final HistoryRepository historyRepository;
    private final HistoryContratRepository historyContratRepository;
    private ContratProduit contratProduit;
    private List<Agence> agenceList;
    private final AgenceRepository agenceRepository;
    private Optional<Produit> produit;
    private List<Module> codeModules;

    private Contrat_Institution contrat;
    private int nbr;


    public Institution creerContrat(Contrat request){
        Optional<Institution> institutionOptional = institutionRepository.findByCodeInst(request.getInstitution());
        if(institutionOptional.isEmpty()) throw new ProduitException("aucune institution avec le code "+request.getInstitution());
        institution = institutionOptional.get();

    List<ContratUnit> contratUnitList = new ArrayList<>();
        request.getContratUnits().forEach(contratUnit -> {
            Optional<Produit> produitOptional = produitRepository.findByCodeProduit(contratUnit.getProduit());
            if(produitOptional.isEmpty() || produitOptional.get().getStatut() == 2)
                throw new ProduitException("aucun produit avec le code "+ contratUnit.getProduit());
            contratUnitList.add(contratUnit);
        });
        if(contratUnitList.isEmpty()) throw new ProduitException("Aucun produit ne correspond aux produits que vous avez choisi");

        contratUnitList.forEach(contratUnit->{

            Optional<Produit> produitOptional = produitRepository.findByCodeProduit(contratUnit.getProduit());

            if(produitOptional.isEmpty()) throw new ProduitException("Aucun produit ne correspond au code "+ contratUnit.getProduit());
            var produit = produitOptional.get();

            Optional<Contrat_Institution> contratInstitutionOptional = contratRepository.findByInstitutionAndProduit(institution,produit);
            if(contratInstitutionOptional.isPresent() && contratInstitutionOptional.get().getStatut() == 1 )
                throw new ProduitException("le Produit "+produit.getNom()+" est déjà actif pour " +institution.getNomInst()
                        );

            contrat = new Contrat_Institution();
            contrat.setCodeContrat(CodeGenerator.codeContrat(institution.getNomInst(),contratUnit.getProduit()));
            contrat.setLibelleContrat("Activation de "+produit.getNom()+" à "+institution.getNomInst());
            contrat.setInstitution(institution);
            contrat.setProduit(produit);
            contrat.setDateDebut(now());
            contrat.setTypeContrat(contratUnit.getTypeContrat());
            switch (contrat.getTypeContrat()){
                case "AgenceLimité_PosteLimité":{
                    contrat.setNbrAgence(contratUnit.getNbrAgence());
                    contrat.setNbrPosteTotal(contratUnit.getNbrPoste());
                    if(contratUnit.getAgences().size() >  contratUnit.getNbrAgence()) throw new
                            ProduitException("le nombre d'agence précisé dans le contrat est " +
                            "inférieur au nombre d'agence séléctionné");
                }
                break;
                case "AgenceLimité_PosteIllimité":{
                    contrat.setNbrAgence(contratUnit.getNbrAgence());
                    contrat.setNbrPosteTotal(0);
                    if(contratUnit.getAgences().size() >  contratUnit.getNbrAgence()) throw new
                            ProduitException("le nombre d'agence précisé dans le contrat est " +
                            "inférieur au nombre d'agence séléctionné");
                }
                break;

                case "AgenceIllimité_PosteIllimité":{
                    contrat.setNbrAgence(0);
                    contrat.setNbrPosteTotal(0);
                }
                break;
                default:{
                    throw new ProduitException("vous devez fournir le type de contrat");
                }
            }
            contrat.setStatut(1);
            contrat =  contratRepository.save(contrat);

            contratUnit.getAgences().forEach(codeagence->{

              var  agence = agenceRepository.findByInstitutionAndCodeAgenceAndStatut(
                   institution, codeagence,1);
              if(agence == null ) throw new ProduitException("cet agence n'appartient pas à cette institution ");

              List<ActivationModule> moduleActivation = new ArrayList<>();
              if(Objects.nonNull(contratUnit.getModules())){
                  contratUnit.getModules().forEach(module->{
                      var activeModule = new ActivationModule()
                              ;                   activeModule.setCodeModule(module);
                      activeModule.setNbrPoste(0);

                      moduleActivation.add(activeModule);
                  });
              }
              else throw new ProduitException("Liste de module vide");


              var requete = ActivationRequest.builder()
                      .codeAgence(codeagence)
                      .codeInst(institution.getCodeInst())
                      .codeProduit(produit.getCodeProduit())
                      .modules( moduleActivation)
                      .build();
              agenceService.activerModule(requete);
                    }

            );



            var historique = new Historique();
            historique.setAction("Activation du produit "+ produit.getNom()+" pour l'institution "+institution.getNomInst());
            historique.setStatut(1);
            historique.setDateCreation(LocalDateTime.now());
            historique.setAuteur("");

            historyRepository.save(historique);
        });

        return institution;
    }

    public List<DetailContratInst> lister(){
        List<Object[]> results = contratRepository.listeReturn();
        List<DetailContratInst> details = new ArrayList<>();
        for(Object[] result: results){
            String codeinst = (String) result[0];
            String nominst = (String) result[1];
            String typearchitecture = (String) result[2];
            int nbrProduit = ((Number) result[3]).intValue();

            var detail = new DetailContratInst();

            detail.setNbrProduit(nbrProduit);
            detail.setCodeInst(codeinst);
            detail.setTypeArchitecture(typearchitecture);
            detail.setNomInst(nominst);

            details.add(detail);

        }
        return details;
    }

    public List<Contrat_Institution> allByInstitution(String codeinst){
        Optional<Institution> institutionOptional = institutionRepository.findByCodeInstAndStatut(codeinst,1);

        if(institutionOptional.isEmpty()) throw new ProduitException("aucune institution avec le code "+codeinst);

        return contratRepository.findAllByInstitutionAndStatut(institutionOptional.get(),1);
    }

    public Institution activerModule(String institutionCode){
        Optional<Institution> institutionOptional = institutionRepository.findByCodeInstAndStatut(institutionCode,1);

        if(institutionOptional.isEmpty()) throw new ProduitException("aucun produit avec le code "+institutionCode);
        institution = institutionOptional.get();
        List<Agence> listeAgence = new ArrayList<>() ;
        List<Contrat_Institution> listeContrat = new ArrayList<>();
        listeAgence = agenceService.ListeParInstitution(institutionCode);
        listeContrat = contratRepository.findAllByInstitutionAndStatut(institution,1);
        if(listeContrat.isEmpty()) throw new ProduitException("aucun produit actif pour cette institution!!");
        if(listeAgence.isEmpty()) throw new ProduitException("liste d'agence vide!!");
        List<Contrat_Institution> finalListeContrat = listeContrat;
        listeAgence.forEach(agence->{
           finalListeContrat.forEach(contrat->{
               if(contrat.getDateFin().isBefore(now())) throw new ProduitException("le contrat de "+contrat.getProduit().getNom()
                       +" a expiré"
               );
               agenceService.activerProduit(contrat.getProduit().getCodeProduit(), agence.getCodeAgence());
           });
        });

        return institution;
    }

    public Contrat_Institution ajoutAvenant(ContratUnit contratUnit){
        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(contratUnit.getProduit(),1);

        if(produitOptional.isEmpty()) throw new ProduitException("aucun produit ne correspond au code "+ contratUnit.getProduit());

        Optional<Contrat_Institution> contratInstitutionOptional = contratRepository.findByProduitAndCodeContratAndStatut(produitOptional.get()
                ,contratUnit.getCodeContrat(),1);
        if(contratInstitutionOptional.isEmpty()) throw new ProduitException("aucun contrat n'existe avec ce code pour ce produit!!");

        contrat = contratInstitutionOptional.get();
        var histoContrat = new HistoContrat();
        histoContrat.setCodeContrat(contratUnit.getCodeContrat());
        histoContrat.setLibelleContrat(contrat.getLibelleContrat());
        histoContrat.setDateDebut(contrat.getDateDebut());
        histoContrat.setInstitution(contrat.getInstitution());
        histoContrat.setDateFin(contrat.getDateFin());
        histoContrat.setTypeContrat(contrat.getTypeContrat());
        histoContrat.setType(contrat.getType());
        histoContrat.setProduit(contrat.getProduit());
        histoContrat.setNbrPosteTotal(contrat.getNbrPosteTotal());
        histoContrat.setNbrAgence(contratUnit.getNbrAgence());
        historyContratRepository.save(histoContrat);

        var produit = produitOptional.get();
        contrat.setType("a");
        contrat.setLibelleContrat("Avenant au contrat de "+produit.getNom());
        contrat.setTypeContrat(contratUnit.getTypeContrat());
        contrat.setDateDebut(convertDateToLocalDateTime(contratUnit.getDateDebut()));
        contrat.setDateFin(convertDateToLocalDateTime(contratUnit.getDateFin()));
        switch (contratUnit.getTypeContrat()){
            case "AgenceLimité_PosteLimité":{
                contrat.setNbrAgence(contratUnit.getNbrAgence());
                contrat.setNbrPosteTotal(contratUnit.getNbrPoste());
            }
            break;
            case "AgenceLimité_PosteIllimité":{
                contrat.setNbrAgence(contratUnit.getNbrAgence());
                contrat.setNbrPosteTotal(0);
            }
            break;

            case "AgenceIllimité_PosteIllimité":{
                contrat.setNbrAgence(0);
                contrat.setNbrPosteTotal(0);
            }
            break;
            default:{
                throw new ProduitException("vous devez fournir le type de contrat");
            }
        }

        return contratRepository.save(contrat);
    }

    public List<Produit> productsByInst(String codeinst){
        institution = institutionService.OneInstitution(codeinst);
        List<Contrat_Institution> contratList = contratRepository.findAllByInstitutionAndStatut(institution, 1);
        List<Produit> produitList = new ArrayList<>();

        if(contratList.isEmpty()){
            produitRepository.findAllByStatut(1).forEach(produit->{
                produitList.add(produit);
            });
        }
        contratList.forEach(contrat->{
            produitRepository.findAllByStatut(1).forEach(produit->{
                if(!produit.equals(contrat.getProduit())){
                    if(!produitList.contains(produit)){
                        produitList.add(produit);
                    }

                }
            });
        });

        return produitList;
    }

    public Contrat_Institution getOne(String code){
        Optional<Contrat_Institution> contratInstitutionOptional = contratRepository.findByCodeContrat(code);
        contrat = contratInstitutionOptional.get();
        if(contratInstitutionOptional.isEmpty() || contrat.getStatut() == 2)
            throw new ProduitException("aucun contrat avec le code "+code);

        return contrat;
    }

    public Contrat_Institution getByInstitution(Institution institution){
        Optional<Contrat_Institution> contratInstitutionOptional = contratRepository.findByInstitutionAndStatut(institution,1);
        if(contratInstitutionOptional.isEmpty()) throw new ProduitException("Aucun contrat actif pour l'institution "
                +institution.getNomInst());
        return contratInstitutionOptional.get();
    }

    public List<Institution> listeInstitution(){
        listeInstitution = new ArrayList<>();
        contratRepository.findAllInstitution().forEach(codeInst->{
            Optional<Institution> institutionOptional = institutionRepository.findByCodeInstAndStatut(codeInst,1);
            if(institutionOptional.isPresent()){
                listeInstitution.add(institutionOptional.get());
            }
        });

        return listeInstitution;
    }

    public List<Institution> uneListeInstitution(){
        List<Object[]> results = contratRepository.listerInst();
        listeInstitution = new ArrayList<>();

        for(Object[] result: results){
            String codeinst = (String)result[0];
            String nominst = (String)result[1];
            String adresseinst = (String)result[2];
            int statut = ((Number)result[3]).intValue();

            institution = new Institution();
            institution.setCodeInst(codeinst);
            institution.setNomInst(nominst);
            institution.setAdresseInst(adresseinst);
            institution.setStatut(statut);

            listeInstitution.add(institution);
        }

        return listeInstitution;
    }

    public List<Produit> listeProduitParInstitution(String codeInstitution){
        listeProduit = new ArrayList<>();
        institution = institutionService.OneInstitution(codeInstitution);
        if(institution== null) throw
             new ProduitException("cette institution n'existe pas!");
        List<Object[]> results = agenceRepository.products(institution.getCodeInst());
        for(Object[] result: results){
            String code = (String) result[0];
            String nom = (String) result[1];

            var produit = new Produit();
            produit.setCodeProduit(code);
            produit.setNom(nom);


            listeProduit.add(produit);
        }
        return listeProduit;
    }

    public List<Module> listemoduleByProduit(String codeProduit){
        List<Object[]> results = contratRepository.findModuleByProduit(codeProduit);
        if(results.isEmpty()) throw  new ProduitException("liste vide!!");

        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(codeProduit,1);

        if(produitOptional.isEmpty()) throw new ProduitException("aucune institution avec le code "+ codeProduit);
        modules = new ArrayList<>();
        for (Object[] result : results) {
            String codemodule = (String) result[0];
            String libellemodule = (String) result[1];
            String description = (String) result[2];
            int statut = ((Number) result[3]).intValue();
            String codeproduit = (String) result[4];
            Date dateCreation = ((Date) result[5]);
            String type_module = (String) result[6];

            Module module = new Module();

            module.setCodeModule(codemodule);
            module.setLibelleModule(libellemodule);
            module.setStatut(statut);
            module.setDescription(description);
            module.setDateCreation(dateCreation.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            module.setProduit(produitOptional.get());
            module.setTypeModule(type_module);

            modules.add(module);


        }

        return modules;
    }

    public List<String> produitList(String codeinst){
        List<String> codes = new ArrayList<>();
        List<Object[]> results = contratRepository.listeProduit(codeinst);
        for(Object[] result: results){
            String code = (String) result[0];
            codes.add(code);
        }
        return codes;
    }

    public DetailContrat detailContrat(String codeAgence){
        List<Object[]> results = contratRepository.unDetail(codeAgence);
        DetailContrat detailContrat = new DetailContrat();
        for(Object[] result : results){
            if(result != null){
                String codedetail = (String) result[0];
                int statut = ((Number) result[5]).intValue();
                String codeAg = (String) result[6];
                Agence agence = agenceService.uneAgence(codeAg);

                detailContrat.setCodeDetailContrat(codedetail);
                detailContrat.setAgence(agence);
                detailContrat.setStatut(statut);
            }
        }

        return  detailContrat;
    }

    public AgenceDetail detailagence( String codeInst, String codeAgence){
        Optional<Institution> institutionOptional = institutionRepository.findByCodeInstAndStatut(codeInst,1);
        if(institutionOptional.isEmpty())
            throw new ProduitException("Aucune institution avec le code "+codeInst);
        institution = institutionOptional.get();
        Agence agence = agenceService.findAgence(institution, codeAgence);
        if(agence == null) throw new ProduitException("Aucune agence de l'institution "+ institution.getNomInst()+" avec le code "+codeAgence);

        contrat = contratRepository.findByInstitutionAndStatut(institution,1).get();
        if(contrat == null) throw new ProduitException("Cette institution n'a pas de contrat");

        List<String> codeProduits = new ArrayList<>();

        contratProduitRepository.findAllByContratInstitutionAndStatut(contrat.getCodeContrat(), 1).forEach(cp->{
            codeProduits.add(cp.getProduit());
        });

        List<Produit> produits = new ArrayList<>();
        List<ProduitDetail> produitDetails = new ArrayList<>();

        codeProduits.forEach(codeprod->{
          produit =  produitRepository.findByCodeProduitAndStatut(codeprod,1);
          if(produit.isPresent()){
              produits.add(produit.get());
          }
        });

        var agenceDetail = new AgenceDetail();

        agenceDetail.setCodecontrat(contrat.getCodeContrat());
        agenceDetail.setTypeContrat(contrat.getTypeContrat());
        agenceDetail.setNbrPosteTotal(contrat.getNbrPosteTotal());
        agenceDetail.setNbrAgence(contrat.getNbrAgence());
        produits.forEach(produit->{
            var produitDetail = new ProduitDetail();

            List<ModuleDetail> moduleDetails = new ArrayList<>();

            produitDetail.setCodeProduit(produit.getCodeProduit());
            produitDetail.setLibelle(produit.getNom());
            List<Object[]> results = contratRepository.moduleDetail(agence.getCodeAgence(), produit.getCodeProduit());

            if(results != null){
                for(Object[] result : results){
                    String codemodule = (String) result[0];
                    String libellemodule = (String) result[1];
                    int nbrLicenceEnAttente = ((Number) result[2]).intValue();
                    int nbrLicenceActive = ((Number) result[3]).intValue();
                    int nbrLicenceInactive = ((Number) result[4]).intValue();

                    var moduledetail = new ModuleDetail();

                    moduledetail.setCodeModule(codemodule);
                    moduledetail.setLibelleModule(libellemodule);
                    moduledetail.setNbrLicenceEnAttente(nbrLicenceEnAttente);
                    moduledetail.setNbrLicenceActive(nbrLicenceActive);
                    moduledetail.setNbrLicenceInactive(nbrLicenceInactive);

                    moduleDetails.add(moduledetail);
                }

                produitDetail.setModules(moduleDetails);

                List<Object[]> resultats = contratRepository.nbrPosteDetail(agence.getCodeAgence(), produit.getCodeProduit());

                for(Object[] result : resultats){
                    int nbrPoste = ((Number) result[0]).intValue();

                    produitDetail.setNbrPosteProduit(nbrPoste);
                }

            }



        });

        List<Object[]> answers = contratRepository.posteActive(agence.getCodeAgence());

        for(Object[] result : answers){
            int nbrPosteActive = ((Number) result[0]).intValue();

            agenceDetail.setNbrPosteActif(nbrPosteActive);
        }

    return agenceDetail;
    }

    public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert) {
        Instant instant = dateToConvert.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }


}
