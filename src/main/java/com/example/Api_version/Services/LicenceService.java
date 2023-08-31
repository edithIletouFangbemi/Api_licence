package com.example.Api_version.Services;

import com.example.Api_version.config.AESCryptor;
import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.*;
import com.example.Api_version.request.*;
import com.example.Api_version.utils.CodeGenerator;

import org.jfree.data.time.Day;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.LocalDateTime.now;

@Service
public class LicenceService {
    private final LicenceRepository licenceRepository;
    private final PosteRepository posteRepository;
    private final ContratService contratService;
    private final ParametreService parametreService;
    private final InstitutionService institutionService;
    private final AgenceService agenceService;
    private final ModuleService moduleService;
    private final ParametreDeVieRepository parametreDeVieRepository;
    private Poste postenew;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String returnKey = "#@£%&é'(-è_çà)df";
    private final ProduitRepository produitRepository;
    private final DetailContratRepository detailContratRepository;
    private final ContratRepository contratRepository;
    private final LicenceModuleRepository licenceModuleRepository;

    public LicenceService(LicenceRepository licenceRepository, PosteRepository posteRepository, ContratService contratService, ParametreService parametreService, InstitutionService institutionService, AgenceService agenceService, ModuleService moduleService, ParametreDeVieRepository parametreDeVieRepository, ProduitRepository produitRepository, DetailContratRepository detailContratRepository, ContratRepository contratRepository, LicenceModuleRepository licenceModuleRepository) {
        this.licenceRepository = licenceRepository;
        this.posteRepository = posteRepository;
        this.contratService = contratService;
        this.parametreService = parametreService;
        this.institutionService = institutionService;
        this.agenceService = agenceService;
        this.moduleService = moduleService;
        this.parametreDeVieRepository = parametreDeVieRepository;
        this.produitRepository = produitRepository;
        this.detailContratRepository = detailContratRepository;
        this.contratRepository = contratRepository;
        this.licenceModuleRepository = licenceModuleRepository;
    }

    public List<Institution> institutionList(){
        List<Object[]> results = licenceRepository.listeInstitution();

        List<Institution> listInst = new ArrayList<>();

        for(Object[] result : results){
            String codeinst = (String) result[0];
            String nominst = (String) result[4];
            int statut = ((Number) result[5]).intValue();

            Institution inst = new Institution();
            inst.setCodeInst(codeinst);
            inst.setNomInst(nominst);
            inst.setStatut(statut);

            listInst.add(inst);
        }
        return  listInst;
    }

    public List<Agence> agenceList(String codeinst,String codeproduit){
        List<Object[]> results = licenceRepository.listeAgence(codeinst, codeproduit);

        List<Agence> listAgence = new ArrayList<>();

        for(Object[] result : results){
            String codeagence = (String) result[0];
            String nom = (String) result[5];
            int statut = ((Number) result[6]).intValue();

            Agence agence = new Agence();
            agence.setCodeAgence(codeagence);
            agence.setNom(nom);
            agence.setStatut(statut);

            listAgence.add(agence);
        }
        return  listAgence;
    }

    public List<Produit> produitList(String codeinst){
        List<Object[]> results = licenceRepository.listeProduit(codeinst);

        List<Produit> listProduit = new ArrayList<>();

        for(Object[] result : results){
            String codeproduit = (String) result[0];
            String libelle = (String) result[4];
            int statut = ((Number) result[5]).intValue();

            Produit produit = new Produit();
            produit.setCodeProduit(codeproduit);
            produit.setNom(libelle);
            produit.setStatut(statut);

            listProduit.add(produit);
        }
        return  listProduit;
    }

    public List<Module> moduleList(String codeagence, String codeproduit){
        List<Object[]> results = licenceRepository.modules(codeagence,codeproduit);

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

    public List<LicenceReturnRequest> genererLicence(LicenceRequest request) throws Exception {
        Institution institution = institutionService.OneInstitution(request.getInstitution());
        if(institution == null) throw new ProduitException("cette institution n'existe pas");
        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(request.getProduit(),1);
        if(produitOptional.isEmpty()) throw new ProduitException("aucun produit avec le code "+ request.getProduit());
        Contrat_Institution contrat = contratRepository.findByInstitutionAndProduit(institution, produitOptional.get()).get();

        if(contrat == null) throw new ProduitException("cette institution n'a aucun contrat pour ce produit!");

        Agence agence = agenceService.findAgence(institution, request.getAgence());
        if(agence == null) throw new ProduitException("aucune agence avec ce nom dans la liste des agences de "+institution.getNomInst());

        List<Module> modules = moduleService.moduleByProduit(request.getProduit());
        List<Module> moduleList = new ArrayList<>();
        List<String> moduleInRequest = new ArrayList<>();
        moduleInRequest = request.getModules();
        if(modules.isEmpty()) throw new ProduitException("ce produit n'as pas de module à activer");
        List<String> finalModuleInRequest = moduleInRequest;
        modules.forEach(module -> {
            if(finalModuleInRequest.contains(module.getCodeModule())){
                DetailContrat detail = detailContratRepository.findByAgenceAndModuleAndStatut(agence, module,1).get();
                if(detail != null){
                    moduleList.add(module);
                }
                else throw new ProduitException("ce module n'est pas actif pour cette agence");

            }
        });

            if(contrat.getTypeContrat().equals("AgenceLimité_PosteLimité")){
                moduleList.forEach(mod->{
                    DetailContrat detail = detailContratRepository.findByAgenceAndModuleAndStatut(agence, mod,1).get();
                    if(mod.getTypeModule().equals("standard")){
                        List<Object[]> results = licenceRepository.countLicenceByModuleByAgence(institution.getCodeInst(),mod.getCodeModule(), agence.getCodeAgence());

                        int nbrLicence = 0;
                        for(Object[] result : results){
                            nbrLicence = ((Number) result[0]).intValue();
                        }

                        if(nbrLicence >= detail.getNbrPoste()) throw new ProduitException(
                                "le nombre total de licence pour cette agence pour ce module est "+contrat.getNbrPosteTotal()+" et ce nombre est atteint"
                        );
                    }
                    if(mod.getTypeModule().equals("additionnel")){
                        List<Object[]> results = licenceRepository.countLicenceByModuleByAgence(agence.getCodeAgence(),mod.getCodeModule());
                        int nbrLicence = 0;
                        for(Object[] result : results){
                            nbrLicence = ((Number) result[0]).intValue();
                        }
                        DetailContrat detailContrat = detailContratRepository.findByAgenceAndModuleAndStatut(agence, mod,1).get();
                        if(nbrLicence >= detailContrat.getNbrPoste() && detailContrat.getTypeContratModule()=="limitee") throw new ProduitException(
                                "le nombre total de licence pour cette agence pour ce module es atteint et c'est "+ detailContrat.getNbrPoste()
                        );

                    }

                });

            }

        Optional<Poste> posteOptional = posteRepository.findByAdresseIpAndAdresseMacAndIdMachineAndIdDisque(
                request.getAdresseIp(),
                request.getAdresseMac(),
                request.getIdMachine(),
                request.getIdDisqueDur()
        );

        if(posteOptional.isPresent()) {
            moduleList.forEach(mod->{
                    Optional<Licence> licenceOptional = licenceRepository.findByPosteAndModule(posteOptional.get(), mod);

                    if(licenceOptional.isPresent()) {

                        throw new ProduitException("Un poste a deja ces informations veuillez changer de poste pour le module" + mod.getLibelleModule());
                    }

                    postenew = posteOptional.get();

            });
        }



        if(moduleList.isEmpty()) throw new ProduitException("ce produit ne contient pas les modules précisés!");
        Parametre param = parametreService.activeParam();
        if(param == null) throw new ProduitException("Aucun parametre en cours pour générer la licence!");
        String[] parametres = param.getDescription().split(",");
        Poste[] poste = {new Poste()};
        if(postenew == null){
            poste[0].setCodePoste(CodeGenerator.codeDetailContrat(request.getAdresseIp()));
            poste[0].setAgence(agence);
            poste[0].setAdresseIp(request.getAdresseIp());
            poste[0].setAdresseMac(request.getAdresseMac());
            poste[0].setIdDisque(request.getIdDisqueDur());
            poste[0].setIdMachine(request.getIdMachine());
            poste[0].setDateCreation(now());
            poste[0].setStatut(1);
        }
        else{
            poste = new Poste[]{postenew};
        }


        StringBuilder value = new StringBuilder();
        StringBuilder key = new StringBuilder();
        StringBuilder keyReturn = new StringBuilder();

        int startIndex;
        for(String parm : parametres){
            if("adresseIp".equals(parm)){
                startIndex = poste[0].getAdresseIp().length() - 6;
                if(startIndex >=0){
                    value.append(poste[0].getAdresseIp());
                    key.append(poste[0].getAdresseIp().substring(startIndex, poste[0].getAdresseIp().length()));
                    keyReturn.append("Ip");
                }
                else {
                    throw new ProduitException("l'adresse Ip doit depasser 6 caractères!");
                }

            }
            if("adresseMac".equals(parm)){
                startIndex = poste[0].getAdresseMac().length() - 6;
                if(startIndex >=0){
                    value.append(poste[0].getAdresseMac());
                    key.append(poste[0].getAdresseMac().substring(startIndex, poste[0].getAdresseMac().length()));
                    keyReturn.append("Mac");
                }
                else {
                    throw new ProduitException("l'adresse Mac doit depasser 6 caractères!");
                }
            }
            if("idMachine".equals(parm)){
                startIndex = poste[0].getIdMachine().length() - 5;

                if(startIndex>=0){
                    value.append(poste[0].getIdMachine());
                    key.append(poste[0].getIdMachine().substring(startIndex, poste[0].getIdMachine().length()));
                    keyReturn.append("IdMachine");
                }
                else {
                    throw new ProduitException("l'identifiant de la  machine doit depasser 5 caractères!");
                }
            }
            if("idDisqueDur".equals(parm)){
                startIndex = poste[0].getIdDisque().length() - 5;
                value.append(poste[0].getIdDisque());
                if(startIndex>=0){
                    value.append(poste[0].getIdDisque());
                    key.append(poste[0].getIdDisque().substring(startIndex, poste[0].getIdDisque().length()));
                    keyReturn.append("IdDisqueDur");
                }
                else {
                    throw new ProduitException("l'identifiant du disque dur doit depasser 6 caractères!");
                }

            }
        }
        keyReturn.append(CodeGenerator.keyReturn());
        List<LicenceReturnRequest> resultats = new ArrayList<>();

        Poste[] finalPoste = poste;
        Poste[] finalPoste1 = poste;
        moduleList.forEach(mod->{
            String licence = "";
            try {
                licence = AESCryptor.encryptLicence(value.toString(), CodeGenerator.generateKey(key.toString(), mod.getLibelleModule()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Licence licence1 = new Licence();

            licence1.setCodeLicence(CodeGenerator.codeLicenceserveur(institution.getCodeInst(),agence.getNom()));
            licence1.setStatut(1);

            finalPoste[0] = posteRepository.save(finalPoste[0]);

            licence1.setPoste(finalPoste[0]);
            licence1.setDateCreation(now());
            licence1.setLibelle(passwordEncoder.encode(licence));
            licence1.setKey(key.toString());
            licence1.setModule(mod);
            licence1.setDateExpiration(LocalDateTime.now());

            licenceRepository.save(licence1);

            try {

                var reponse = LicenceReturnRequest.builder()
                        .codeLicence(licence)
                        .module(mod.getCodeModule())
                       .institution(institution.getCodeInst())
                        .agence(agence.getCodeAgence())
                        .key(keyReturn.toString())
                        .codePoste(finalPoste1[0].getCodePoste())
                        .build();

                resultats.add(reponse);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }




        });

    return resultats;
    }

    public LicenceReturnRequest getLicence(String codelicence) throws Exception {
        List<Object[]> results = licenceRepository.getLicence(codelicence);
        var retour = new LicenceReturnRequest();
        for(Object[] result: results){
            String libelleLicence = (String)result[0];
            String codemodule = (String)result[1];
            String codeposte = (String)result[2];
            String key = (String)result[3];
            retour = LicenceReturnRequest.builder()
                    .codeLicence(AESCryptor.encrypt(libelleLicence,returnKey))
                    .module(AESCryptor.encrypt(codemodule,returnKey))
                    .key(AESCryptor.encrypt(key,returnKey))
                    .codePoste(AESCryptor.encrypt(codeposte,returnKey))
                    .build();


        }

        return retour;
    }

    public List<LicenceRecapRequest> recapLicence(){
        List<Object[]> results = licenceRepository.listeRecapitulatif();

        List<LicenceRecapRequest> liste = new ArrayList<>();

        for(Object[] result: results){
            String codeinst = (String) result[0];
            String nominst = (String) result[1];
            String typearchitecture = (String) result[2];
            int nbrProduit = ((Number)result[3]).intValue();

            var licenceRequest = new LicenceRecapRequest();


            licenceRequest.setNbrProduitActif(nbrProduit);
            licenceRequest.setNomInst(nominst);
            licenceRequest.setCodeInst(codeinst);
            licenceRequest.setTypeArchitecture(typearchitecture);



            liste.add(licenceRequest);

        }

        return liste;
    }

    public List<AgenceRecapRequest> recapAgence(String codeinst){
        List<Object[]> results = licenceRepository.listeRecapAgenceByInst(codeinst);
        List<AgenceRecapRequest> listeRecap = new ArrayList<>();
        for(Object[] result: results){
            String codeagence = (String)result[0];
            String nom = (String)result[1];
            int nbrproduit = ((Number)result[2]).intValue();
            int nbrmodule = ((Number)result[3]).intValue();

            var recap = new AgenceRecapRequest();
            recap.setCodeAgence(codeagence);
            recap.setNbrModuleActif(nbrmodule);
            recap.setNbrProduitActif(nbrproduit);
            recap.setNom(nom);

            listeRecap.add(recap);
        }

        return listeRecap;
    }

    public List<PosteDetailRequest> recapPoste(String codeagence){
        List<Object[]> results = licenceRepository.listeRecapPosteLicence(codeagence);
        List<PosteDetailRequest> listePoste = new ArrayList<>();

        for(Object[] result: results){
            String codeproduit = (String)result[0];
            String nom = (String)result[1];
            String codemodule = (String)result[2];
            String libelleModule = (String)result[3];
            String codeposte = (String)result[4];
            String libelleposte = (String)result[5];
            String codelicence = (String)result[6];
            int licenceStatut = ((Number) result[7]).intValue();

            var recap = new PosteDetailRequest();

            recap.setCodemodule(codemodule);
            recap.setCodePoste(codeposte);
            recap.setLibellePoste(libelleposte);
            recap.setLibelleModule(libelleModule);
            recap.setCodeProduit(codeproduit);
            recap.setLicenceStatut(licenceStatut);
            recap.setNomProduit(nom);
            recap.setCodeLicence(codelicence);

            listePoste.add(recap);
        }

        return listePoste;
    }

    public int allLicence(){
        return licenceRepository.findAllByStatut(1).size();
    }

    public void download(String codeLicence){
        List<Object[]> results = licenceRepository.ListeRecapLicence();

       // List<LicenceRecapRequest> liste = new ArrayList<>();

        for(Object[] result: results){
            String libelle = (String) result[0];
            String codeagence = (String) result[1];
            String codeinst = (String) result[2];


        }
    }

    public CheckReturn checkLicence(LicenceReturnRequest request) throws Exception {
        String key = AESCryptor.decrypt(request.getKey(), returnKey);
        String libelle = AESCryptor.decrypt(request.getCodeLicence(),returnKey);
        String codeAgence = AESCryptor.decrypt(request.getAgence(),returnKey);
        String codeInstitution = AESCryptor.decrypt(request.getInstitution(),returnKey);
        String codeModule = AESCryptor.decrypt(request.getCodeLicence(),returnKey);
        String codePoste = AESCryptor.decrypt(request.getCodePoste(), returnKey);

        Institution institution = institutionService.OneInstitution(codeInstitution);
        if(institution == null || institution.getStatut() == 2) throw new ProduitException("Aucune institution avec le code "+ codeInstitution);
        var agence = agenceService.findAgence(institution, codeAgence);

        if(agence == null || agence.getStatut() == 2) throw new ProduitException("Aucune agence avec le code "+ codeAgence);

        Optional<Poste> posteOptional = posteRepository.findByAgenceAndCodePosteAndStatut(agence,codePoste,1);

        if(posteOptional.isEmpty()) throw new ProduitException("Aucun poste n'existe avec le code "+codePoste);

        Module module = moduleService.read(codeModule);

        if(module == null) throw new ProduitException("ce module n'existe pas");
        var poste = new Poste();
        poste = posteOptional.get();
        List<Licence> listeLicence = new ArrayList<>();
        AtomicReference<Licence> oldLicence = new AtomicReference<>(new Licence());
        listeLicence = licenceRepository.findAllByPoste(poste);
        listeLicence.forEach(licence -> {
            if(passwordEncoder.matches(libelle,licence.getLibelle())){
                oldLicence.set(licence);
            }
        });
        if(oldLicence == null) throw new ProduitException("Aucune licence ne correspond à celle ci!!");

        var response = new CheckReturn();
        switch (oldLicence.get().getStatut()){
            case 0: {
                oldLicence.get().setStatut(1);
                oldLicence.get().setDateEffet(now());
                oldLicence.get().setDateExpiration(now());

                licenceRepository.save(oldLicence.get());

                response.setMessage("1");
            }
            break;
            case 1:{
                response.setMessage("1");
            }
            break;

            case 2: {
                response.setMessage("2");
            }
        }

        response.setModule(module.getLibelleModule());


        return response;

    }

    public LocalDateTime getDate(LocalDateTime date, String typeLicence){
        var paramVie = new ParametreDeVieLicence();
        Optional<ParametreDeVieLicence> parametreDeVieLicenceOptional = parametreDeVieRepository.findByStatutAndTypeLicence(1,typeLicence);
        if(parametreDeVieLicenceOptional.isEmpty()) throw new ProduitException("aucun parametre en cours actuellement!!");
        paramVie = parametreDeVieLicenceOptional.get();

        int duree = paramVie.getQuantite();
        int typeDuree = 0;
        int dureeTotal;
       switch (paramVie.getTypeParametre()){
           case "mois":{
               typeDuree = 30;
           }
           break;
           case "jour":{
               typeDuree = 1;
           }
           break;
           case "annee":{
               typeDuree = 365;
           }
       }

       dureeTotal = duree * typeDuree ;

       date = date.plusDays(dureeTotal);

       return date;
    }
}
