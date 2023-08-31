package com.example.Api_version.Services;

import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import com.example.Api_version.exceptions.AgenceException;
import com.example.Api_version.exceptions.InstitutionException;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.*;
import com.example.Api_version.request.ActivationRequest;
import com.example.Api_version.request.ActiverRequest;
import com.example.Api_version.request.AgenceRequest;
import com.example.Api_version.utils.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.Duration;

import static java.time.LocalDateTime.now;


@Service
@RequiredArgsConstructor
public class AgenceService {
    private final AgenceRepository agenceRepository;
    private final InstitutionRepository institutionRepository;
    private final ContratRepository contratRepository;
    private final DetailContratRepository detailContratRepository;
    private final DetailContratModuleRepository detailContratModuleRepository;
    private Institution institution;
    private Contrat_Institution contrat;
    private List<Module> someModules;
    private Module module;
    private final ModuleService moduleService;
    private DetailContrat detailContrat;
    private DetailContratModule detailContratModule;
    private final ProduitService produitService;
    private Produit produit;
    private Agence agence;
    private String caractere;
    private int nbrAgence;

    public Agence creer(String nom, String description, String adresse, String institutionCode){
        Optional<Institution> inst = institutionRepository.findByCodeInstAndStatut(institutionCode,1);

        institution = inst.get();

        if(inst.isEmpty()) throw  new InstitutionException("aucune institution avec le code "+ institutionCode);
/*
       Optional<Contrat_Institution> contratInstitutionOptional = contratRepository.findByInstitutionAndStatut(institution,1);
        if(contratInstitutionOptional.isPresent()){
            contrat = contratInstitutionOptional.get();
            nbrAgence = this.ListeParInstitution(institution.getCodeInst()).size();
            if((contrat.getTypeContrat().equals("AgenceLimité_PosteIllimité") ||
                    contrat.getTypeContrat().equals("AgenceLimité_PosteLimité") )&& contrat.getNbrAgence() < nbrAgence){

                throw new ProduitException("le nombre " +
                        "d'agence dans le contrat est "+ contrat.getNbrAgence()+
                        " et ce nombre est deja atteint");
            }

        }*/

        Optional<Agence> agenceOptional = agenceRepository.findByNomAndInstitution(nom
                , institution);

        if(agenceOptional.isPresent()) throw new AgenceException("Une agence de l'institution "
                + institution.getNomInst()
                +" existe deja avec le nom " + nom);

        Agence newAgence = new Agence();

        newAgence.setNom(nom);
        newAgence.setCodeAgence(CodeGenerator.generateCode(nom,inst.get().getNomInst()));
        newAgence.setAdresse(adresse);
        newAgence.setDescription(description);
        newAgence.setInstitution(institution);
        newAgence.setStatut(1);
        newAgence.setDateCreation(LocalDateTime.now());
        newAgence = agenceRepository.save(newAgence);
        Agence finalNewAgence = newAgence;
        /*
        if(contrat != null){
            produits( institution.getCodeInst(),contrat.getCodeContrat()).forEach(produit->{
                someModules = modules(produit.getCodeProduit());

                if(someModules.size() > 0){

                    someModules.forEach(module->{
                        detailContrat = new DetailContrat();
                        detailContrat.setCodeDetailContrat(CodeGenerator.codeDetailContrat(finalNewAgence.getNom()));
                        detailContrat.setAgence(finalNewAgence);
                        detailContrat.setNbrPoste(0);
                        detailContrat.setLibelle("activation "+ module.getLibelleModule());
                        detailContrat.setModule(module);
                        detailContrat.setDate_debut(LocalDateTime.now());
                        detailContrat.setStatut(1);
                        detailContratRepository.save(detailContrat);
                    });



                }

            });
        } */

        return newAgence;
    }

    public Agence update(String code, AgenceRequest request){
        Optional<Institution> inst = institutionRepository.findByCodeInst(request.getInstitutionCode());
        Optional<Agence> agenceOptional = agenceRepository.findByCodeAgenceAndInstitution(code,inst.get());
        if(agenceOptional.isEmpty() || agenceOptional.get().getStatut() == 2) throw new AgenceException("aucune agence avec l'identifiant "+code);
        Agence agence = agenceOptional.get();

        agence.setNom(request.getNom());
        agence.setAdresse(request.getAdresse());
        agence.setDescription(request.getDescription());

        return agenceRepository.save(agence);
    }

    public List<Produit> produits(String codeinst, String codeContrat){
        List<Object[]> results = agenceRepository.Produits(codeinst, codeContrat);

        List<Produit> products = new ArrayList<>();
        for(Object[] result: results){
            String code = (String) result[0];
            String nom = (String) result[4];
            int statut = ((Number) result[5]).intValue();

            produit = new Produit();
            produit.setCodeProduit(code);
            produit.setNom(nom);
            produit.setStatut(statut);

            products.add(produit);
        }

        return products;

    }

    public List<Agence> liste(){
        return agenceRepository.findAll();
    }

    public List<Agence> ListeDeleted(){
        return agenceRepository.findAllByStatut(2);
    }

    public List<Agence> ListeParInstitution(String institutionCode){
        Optional<Institution> inst = institutionRepository.findByCodeInst(institutionCode);
        if(inst.isEmpty() || inst.get().getStatut() == 2) throw new InstitutionException("aucune institution avec l'identifiant "+institutionCode);
        return agenceRepository.findAllByInstitutionAndStatut(inst.get(),1);
    }

    public Agence uneAgence(String code){
        Optional<Agence> agence = agenceRepository.findByCodeAgence(code);
        if(agence.isEmpty() || agence.get().getStatut() == 2) throw new AgenceException("aucune agence avec le code "+ code);
        return agence.get();
    }

    public String supprimer(String code){
        Optional<Agence> agence = agenceRepository.findByCodeAgence(code);
        if(agence.isEmpty() || agence.get().getStatut() == 2) throw new AgenceException("aucune agence avec le code "+ code);
        Agence agence1 = agence.get();
        agence1.setStatut(2);
        agenceRepository.save(agence1);

        return "suppression fait avec succès!";
    }

    public Agence findAgence(Institution institution, String codeAgence){
        Optional<Agence> agenceOptional = agenceRepository.findByInstitutionAndCodeAgenceAndStatut(institution,codeAgence,1);

        if(agenceOptional.isEmpty()) throw new ProduitException("agence inexistante dans la liste des agence de "+ institution.getNomInst());
        return agenceOptional.get();
    }

    public Agence activerModule(ActivationRequest request){
        agence = uneAgence(request.getCodeAgence());
        if(agence == null) throw new ProduitException("Agence n'existe pas!");

        institution = institutionRepository.findByCodeInstAndStatut(request.getCodeInst(),1).get();
        if(institution == null) throw new ProduitException("L'institution n'existe pas!!");
        if(!agence.getInstitution().equals(institution)) throw new ProduitException("Cette institution ne correspond" +
                " pas à l'institution de cette agence");
        produit = produitService.read(request.getCodeProduit());
        if(produit == null) throw new ProduitException("Aucun produit avec ce code");
        Optional<Contrat_Institution> contratInstitutionOptional = contratRepository.findByInstitutionAndProduit(institution,produit);
        if(contratInstitutionOptional.isEmpty()) throw new ProduitException("Cet institution n'a pas "+produit.getNom()+" activé");
        contrat = contratInstitutionOptional.get();
        request.getModules().forEach(moduleActivation->{
            module = moduleService.read(moduleActivation.getCodeModule());
            Optional<DetailContrat> detailContratOptional = detailContratRepository.findByAgenceAndModuleAndStatut(
                    agence, module,
                    1
            );
            if(module == null) throw new ProduitException("ce module n'existe pas dans le produit!!");

            if(detailContratOptional.isPresent() &&(detailContratOptional.get().getTypeContratModule() == module.getTypeModule())) throw new
                    ProduitException("ce module est deja actif pour cette agence!!");

            detailContrat = new DetailContrat();
            detailContrat.setCodeDetailContrat(CodeGenerator.codeDetailContrat(agence.getNom()));
            detailContrat.setModule(module);
            detailContrat.setLibelle("Activation de "+ module.getLibelleModule());
            detailContrat.setStatut(1);
            detailContrat.setAgence(agence);
           // detailContrat.setDate_debut(LocalDateTime.parse(moduleActivation.getDateDebut()));
          //  detailContrat.setDate_fin(moduleActivation.getDateFin());
            detailContrat.setNbrPoste(moduleActivation.getNbrPoste());
            detailContrat.setTypeContratModule(moduleActivation.getType());
            detailContratRepository.save(detailContrat);
        });
        return agence;
    }

    public List<Module> moduleList(String codeagence, String codeproduit){
        List<Object[]> results = agenceRepository.moduleProduit(codeagence,codeproduit);

        List<Module> listModule = new ArrayList<>();

        for(Object[] result : results){
            String codemodule = (String) result[0];
            String libelleModule = (String) result[4];
            int statut = ((Number) result[5]).intValue();
            String typeModule = (String) result[6];

            Module module = new Module();
            module.setCodeModule(codemodule);
            module.setLibelleModule(libelleModule);
            module.setStatut(statut);
            module.setTypeModule(typeModule);

            listModule.add(module);
        }
        return  listModule;
    }

    public List<Module> modules(String codeproduit){
        List<Object[]> results = agenceRepository.modules(codeproduit);
        List<Module> listModule = new ArrayList<>();

        for(Object[] result : results){
            String codemodule = (String) result[0];
            String libelleModule = (String) result[4];
            int statut = ((Number) result[5]).intValue();
            String typeModule = (String) result[6];

            Module module = new Module();
            module.setCodeModule(codemodule);
            module.setLibelleModule(libelleModule);
            module.setStatut(statut);
            module.setTypeModule(typeModule);

            listModule.add(module);
        }
        return  listModule;
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
        if(results.isEmpty()) throw new ProduitException("aucun detail contrat pour cette agence");
        DetailContrat detailContrat = new DetailContrat();
        for(Object[] result : results){
            if(result!=null){
                String codedetail = (String)result[0];
                int statut = ((Number)result[5]).intValue();
                String codeagence = (String)result[6];
                Agence agence = agenceRepository.findByCodeAgence(codeAgence).get();
                detailContrat.setCodeDetailContrat(codedetail);
                detailContrat.setAgence(agence);
                detailContrat.setStatut(statut);
            }
        }


        return  detailContrat;
    }

    public Agence activerProduit(String codeProduit, String codeagence){
        Optional<Agence> agenceOptional = agenceRepository.findByCodeAgence(codeagence);
        if(agenceOptional.isEmpty() ) throw new ProduitException("Aucune agence n'existe avec ce code "+codeagence);
        if(agenceOptional.isPresent() && agenceOptional.get().getStatut() == 2) throw new ProduitException("Aucune agence avec le code "+codeagence);
        var agence = agenceOptional.get();
        var produit = produitService.read(codeProduit);

        if(produit == null) throw new ProduitException("Aucun produit avec le code "+codeProduit);

        var contrat = contratRepository.findByInstitutionAndProduit(agence.getInstitution(),produit).get();

        if(contrat != null && contrat.getStatut() == 2 ) throw new ProduitException("le produit "+produit.getNom()
                +" n'est pas actif pour l'institution "+agence.getInstitution().getNomInst());

        var modules = moduleService.moduleByProduit(produit.getCodeProduit());
        modules.forEach(mod->{
            if(mod.getTypeModule() == "standard"){
                var detail = new DetailContrat();

                detail.setAgence(agence);
                detail.setStatut(1);
                detail.setCodeDetailContrat(CodeGenerator.codeDetailContrat(agence.getNom()));
                detail.setModule(mod);
                detail.setLibelle("Activation du module "+ mod.getLibelleModule());
                detail.setDate_debut(Date.from(now().atZone(ZoneId.systemDefault()).toInstant()));
                detail.setDate_fin(getDatefin(contrat));
                detail.setNbrPoste(0);
                detailContratRepository.save(detail);
            }
        });

        return agence;
    }

    public Date getDatefin(Contrat_Institution contrat){
        LocalDateTime dateDebut = contrat.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); // Remplacez avec votre date de début
        LocalDateTime dateFin = contrat.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();   // Remplacez avec votre date de fin

        Duration duree = Duration.between(dateDebut, dateFin);
        long jours = duree.toDays();

        LocalDateTime dateAutre = LocalDateTime.now(); // Remplacez avec votre autre date

        LocalDateTime resultat = dateAutre.plusDays(jours);

        return Date.from(resultat.atZone(ZoneId.systemDefault()).toInstant());
    }


}
