package com.example.Api_version.Services;

import com.example.Api_version.config.AESCryptor;
import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import com.example.Api_version.exceptions.AgenceException;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.repositories.*;
import com.example.Api_version.request.*;
import com.example.Api_version.utils.CodeGenerator;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private Produit produit = new Produit();
    private final LicenceServeurRepository licenceServeurRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String returnKey = "#@£%&é'(-è_çà)df";

    private Licence licence;
    private final ProduitRepository produitRepository;
    private final DetailContratRepository detailContratRepository;
    private final ContratRepository contratRepository;
    private final Sous_ContratRepository sousContratRepository;

    public LicenceService(LicenceRepository licenceRepository, PosteRepository posteRepository, ContratService contratService, ParametreService parametreService, InstitutionService institutionService, AgenceService agenceService, ModuleService moduleService, ParametreDeVieRepository parametreDeVieRepository, LicenceServeurRepository licenceServeurRepository, ProduitRepository produitRepository, DetailContratRepository detailContratRepository, ContratRepository contratRepository, Sous_ContratRepository sousContratRepository) {
        this.licenceRepository = licenceRepository;
        this.posteRepository = posteRepository;
        this.contratService = contratService;
        this.parametreService = parametreService;
        this.institutionService = institutionService;
        this.agenceService = agenceService;
        this.moduleService = moduleService;
        this.parametreDeVieRepository = parametreDeVieRepository;
        this.licenceServeurRepository = licenceServeurRepository;
        this.produitRepository = produitRepository;
        this.detailContratRepository = detailContratRepository;
        this.contratRepository = contratRepository;
        this.sousContratRepository = sousContratRepository;
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

    public List<LicenceReturnRequest2> genererLicence(LicenceRequest request) throws Exception {
        Institution institution = institutionService.OneInstitution(request.getInstitution());
        if(institution == null) throw new ProduitException("aucune institution avec l'identifiant "+request.getInstitution());
        Optional<Produit> produitOptional = produitRepository.findByIdAndStatut(request.getProduit(),1);
        if(produitOptional.isEmpty()) throw new ProduitException("aucun produit avec l'identifiant "+ request.getProduit());
        produit = produitOptional.get();
        //Contrat_Institution contrat = contratRepository.findByInstitutionAndProduit(institution, produitOptional.get()).get();
        Contrat_Institution contrat = contratRepository.findByInstitutionAndStatut(institution, 1).get();
        if(contrat == null) throw new ProduitException("cette institution n'a aucun contrat en cours!");

        Sous_Contrat sousContrat = sousContratRepository.findByContratAndProduitAndStatut(contrat,produit,1).get();
        if(sousContrat == null) throw new AgenceException("le produit "+produit.getNom()+" ne fait pas partie du contrat de "+institution.getNomInst(), HttpStatus.NOT_FOUND);

        Agence agence = agenceService.findAgence(institution, request.getAgence());
        if(agence == null) throw new ProduitException("aucune agence avec ce nom dans la liste des agences de "+institution.getNomInst());

        List<Module> modules = moduleService.moduleByProduit(produit.getId());
        List<Module> moduleList = new ArrayList<>();
        List<Integer> moduleInRequest = new ArrayList<>();
        moduleInRequest = request.getModules();
        if(modules.isEmpty()) throw new ProduitException("ce produit n'as pas de module à activer");
        List<Integer> finalModuleInRequest = moduleInRequest;
        modules.forEach(module -> {
            if(finalModuleInRequest.contains(module.getId())){
                DetailContrat detail = detailContratRepository.findByAgenceAndModuleAndStatut(agence, module,1).get();
                if(detail != null){
                    moduleList.add(module);
                }
                else throw new ProduitException(module.getLibelleModule()+" n'est pas actif pour "+agence.getNom());
            }
        });



          /*  if(sousContrat.getTypeContrat().equals("AgenceLimité_PosteLimité")){
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

            } */

        Optional<Poste> posteOptional = posteRepository.findByAdresseIpAndAdresseMacAndIdMachineAndIdDisque(
                request.getAdresseIp(),
                request.getAdresseMac(),
                request.getIdMachine(),
                request.getIdDisqueDur()
        );

        if(posteOptional.isPresent()) {
            moduleList.forEach(mod->{
                    Optional<Licence> licenceOptional = licenceRepository.findByPosteAndModuleAndStatut(posteOptional.get(), mod, 1);

                    if(licenceOptional.isPresent()) {

                        throw new ProduitException("Un poste a deja ces informations veuillez changer de poste pour le module" + mod.getLibelleModule());
                    }
                    postenew = posteOptional.get();
            });
        }


        if(moduleList.isEmpty()) throw new ProduitException("ce produit ne contient pas les modules précisés!");

        Parametre param = parametreService.getParamCheckingWithDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        if(param == null) throw new ProduitException("Aucun parametre en cours pour générer la licence!");
        String[] parametres = param.getDescription().split(",");
        Poste poste = new Poste();
        if(postenew == null){
            poste.setCodePoste(CodeGenerator.codeDetailContrat(request.getIdDisqueDur(),request.getIdMachine()));
            poste.setAgence(agence);
            poste.setAdresseIp(request.getAdresseIp());
            poste.setAdresseMac(request.getAdresseMac());
            poste.setIdDisque(request.getIdDisqueDur());
            poste.setIdMachine(request.getIdMachine());
            poste.setDateCreation(now());
            poste.setStatut(1);
        }
        else{
            poste = postenew;
        }

        StringBuilder value = new StringBuilder();
        StringBuilder key = new StringBuilder();
        StringBuilder keyReturn = new StringBuilder();

        int startIndex;
        for(String parm : parametres){
            if("adresseIp".equals(parm)){
                startIndex = poste.getAdresseIp().length() - 6;
                if(startIndex >=0){
                    value.append(poste.getAdresseIp());
                    key.append(poste.getAdresseIp().substring(startIndex, poste.getAdresseIp().length()));
                    keyReturn.append("Ip");
                }
                else {
                    throw new ProduitException("l'adresse Ip doit depasser 6 caractères!");
                }

            }
            if("adresseMac".equals(parm)){
                startIndex = poste.getAdresseMac().length() - 6;
                if(startIndex >=0){
                    value.append(poste.getAdresseMac());
                    key.append(poste.getAdresseMac().substring(startIndex, poste.getAdresseMac().length()));
                    keyReturn.append("Mac");
                }
                else {
                    throw new ProduitException("l'adresse Mac doit depasser 6 caractères!");
                }
            }
            if("idMachine".equals(parm)){
                startIndex = poste.getIdMachine().length() - 5;

                if(startIndex>=0){
                    value.append(poste.getIdMachine());
                    key.append(poste.getIdMachine().substring(startIndex, poste.getIdMachine().length()));
                    keyReturn.append("IdMachine");
                }
                else {
                    throw new ProduitException("l'identifiant de la  machine doit depasser 5 caractères!");
                }
            }
            if("idDisqueDur".equals(parm)){
                startIndex = poste.getIdDisque().length() - 5;
                value.append(poste.getIdDisque());
                if(startIndex>=0){
                    value.append(poste.getIdDisque());
                    key.append(poste.getIdDisque().substring(startIndex, poste.getIdDisque().length()));
                    keyReturn.append("IdDisqueDur");
                }
                else {
                    throw new ProduitException("l'identifiant du disque dur doit depasser 6 caractères!");
                }

            }
        }
        keyReturn.append(CodeGenerator.keyReturn());
        List<LicenceReturnRequest2> resultats = new ArrayList<>();

        final Poste[] finalPoste = {poste};
        //Poste finalPoste1 = poste;
        moduleList.forEach(mod->{
            String licence = "";
            try {
                licence = AESCryptor.encryptLicence(agence.getCodeAgence()+"&#"+mod.getCodeModule()+"#&"+value.toString()+"&poste", CodeGenerator.generateKey(key.toString(), mod.getLibelleModule()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Licence licence1 = new Licence();

            licence1.setCodeLicence(CodeGenerator.codeLicenceserveur(institution.getCodeInst(),agence.getNom()));
            licence1.setStatut(1);

            finalPoste[0] = posteRepository.save(finalPoste[0]);

            System.out.println("voici le poste enregistré "+finalPoste[0]);

            licence1.setPoste(finalPoste[0]);
            licence1.setDateCreation(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            licence1.setLibelle(passwordEncoder.encode(licence));
            licence1.setKeyLicencePoste(key.toString());
            licence1.setModule(mod);
            licence1.setParametre(param);
            licence1.setDateExpiration(LocalDateTime.now());

            licenceRepository.save(licence1);

            try {
                var reponse = LicenceReturnRequest2.builder()
                        .codeLicence(licence)
                        .module(mod)
                       .institution(institution)
                        .agence(agence)
                        .key(keyReturn.toString())
                        .typeLicence("poste")
                       // .codePoste(finalPoste[0].getCodePoste())
                        .build();

                resultats.add(reponse);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    return resultats;
    }

    public LicenceReturnRequest downloadLicence(String codelicence) throws Exception {
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

    public DownloadRequest download(int idLicence){
        Licence licence = licenceRepository.downloadLicence(idLicence);

       // List<LicenceRecapRequest> liste = new ArrayList<>();

       var response = new DownloadRequest();
       response.setKey(licence.getKeyLicencePoste());
       response.setCodelicence(licence.getCodeLicence());
       response.setCodemodule(licence.getModule().getCodeModule());
       response.setCodeAgence(licence.getPoste().getAgence().getCodeAgence());
       response.setCodeinst(licence.getPoste().getAgence().getInstitution().getCodeInst());

       return response;
    }

    public CheckReturn checkLicence(LicenceReturnRequest request) throws Exception {
        String key = AESCryptor.decrypt(request.getKey(), returnKey);
        String libelle = AESCryptor.decrypt(request.getCodeLicence(),returnKey);
        String codeAgence = AESCryptor.decrypt(request.getAgence(),returnKey);
        String codeInstitution = AESCryptor.decrypt(request.getInstitution(),returnKey);
        String codeModule = AESCryptor.decrypt(request.getCodeLicence(),returnKey);
        String codePoste = AESCryptor.decrypt(request.getCodePoste(), returnKey);

        Institution institution = institutionService.OneInstitution(1);
        if(institution == null || institution.getStatut() == 2) throw new ProduitException("Aucune institution avec le code "+ codeInstitution);
        var agence = agenceService.findAgence(institution, 1);

        if(agence == null || agence.getStatut() == 2) throw new ProduitException("Aucune agence avec le code "+ codeAgence);

        Optional<Poste> posteOptional = posteRepository.findByAgenceAndCodePosteAndStatut(agence,codePoste,1);

        if(posteOptional.isEmpty()) throw new ProduitException("Aucun poste n'existe avec le code "+codePoste);

        //Module module = moduleService.read(codeModule);
        Module module = moduleService.read(1);

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

    public List<SituationLicenceRequest> situationLicence(
            int agenceId, List<Integer> posteIds, List<Integer> moduleIds,
            String typeLicence, Date dateDebut , Date dateFin, int statut
    ){
        List<Object[]> results = new ArrayList<>();

        if(typeLicence.equals("agence")){
            results = licenceServeurRepository.situationLicenceServeur(
                    agenceId, posteIds, moduleIds,
                    dateDebut, dateFin, statut
            );
        }else if(typeLicence.equals("poste")){
            results = licenceRepository.situationLicence(
                    agenceId, posteIds, moduleIds,
                    dateDebut, dateFin, statut
            );
        }

        List<SituationLicenceRequest> situationLicenceRequests = new ArrayList<>();
        for(Object[] result : results){
            SituationLicenceRequest newSituationRequest = new SituationLicenceRequest();

            newSituationRequest.setIdLicence(((Number)result[0]).intValue());
            newSituationRequest.setLibelleAgence((String)result[1]);
            newSituationRequest.setIdMachine((String)result[2]);
            newSituationRequest.setLibelleModule((String)result[3]);
            newSituationRequest.setDateCreation((Date)result[4]);
            newSituationRequest.setStatut(statut);
            newSituationRequest.setTypeLicence(typeLicence);

            situationLicenceRequests.add(newSituationRequest);
        }

        return situationLicenceRequests;
    }

    public List<Poste> getAllPosteByAgenceAndStatut(int agenceId){
        Agence agence = agenceService.uneAgence(agenceId);
        List<Poste> postes = new ArrayList<>();

        if(agence != null) {
            postes = posteRepository.findAllByAgenceAndStatut(agence,1);
        }

       return postes;
    }

    public List<LicenceReturnRequest2> getLicencesForDownload(List<Integer> listeIdLicence, String typeLicence){
        List<LicenceReturnRequest2> results = new ArrayList<>();

        if(typeLicence.equals("poste")){
            List<Licence> licences = licenceRepository.findAllByIdAndStatut(listeIdLicence,1);

            licences.forEach(licence -> {
                LicenceReturnRequest2 newLicenceReturn = new LicenceReturnRequest2();

                newLicenceReturn.setCodeLicence(licence.getLibelle());
                newLicenceReturn.setKey(licence.getKeyLicencePoste());
                newLicenceReturn.setTypeLicence(typeLicence);
                newLicenceReturn.setAgence(licence.getPoste().getAgence());
                newLicenceReturn.setModule(licence.getModule());
                newLicenceReturn.setDateCreation(licence.getDateCreation());
                newLicenceReturn.setStatut(licence.getStatut());
                newLicenceReturn.setInstitution(licence.getPoste().getAgence().getInstitution());

                results.add(newLicenceReturn);
            });

        }else if(typeLicence.equals("agence")){
            List<LicenceServeur> licences = licenceServeurRepository.findAllByIdAndStatut(listeIdLicence,1);

            licences.forEach(licence -> {
                LicenceReturnRequest2 newLicenceReturn = new LicenceReturnRequest2();

                newLicenceReturn.setCodeLicence(licence.getLibelle());
                newLicenceReturn.setKey(licence.getKeyLicenceServeur());
                newLicenceReturn.setTypeLicence(typeLicence);
                newLicenceReturn.setAgence(licence.getPoste().getAgence());
                newLicenceReturn.setModule(licence.getModule());
                newLicenceReturn.setDateCreation(licence.getDateCreation());
                newLicenceReturn.setStatut(licence.getStatut());
                newLicenceReturn.setInstitution(licence.getPoste().getAgence().getInstitution());

                results.add(newLicenceReturn);
            });
        }

        return results;
    }

    public LicenceReturnRequest activerLicence(int idLicence){
        Optional<Licence> licenceOptional = licenceRepository.findByIdAndStatut(idLicence, 2);

        if(licenceOptional.isEmpty()) throw new AgenceException("Aucune licence avec l'idendtifiant "+idLicence+" n'a été désactivée", HttpStatus.NOT_FOUND);

        licence = licenceOptional.get();
        licence.setStatut(1);
        licence = licenceRepository.save(licence);
        LicenceReturnRequest newRequestReturn = new LicenceReturnRequest();
        newRequestReturn.setAgence(licence.getPoste().getAgence().getNom());
        newRequestReturn.setModule(licence.getModule().getLibelleModule());
        newRequestReturn.setInstitution(licence.getPoste().getAgence().getInstitution().getNomInst());

        return newRequestReturn;
    }

    public LicenceReturnRequest desactiverLicence(int idLicence){
        Optional<Licence> licenceOptional = licenceRepository.findByIdAndStatut(idLicence, 1);

        if(licenceOptional.isEmpty()) throw new AgenceException("Aucune licence avec l'idendtifiant "+idLicence+" n'est active", HttpStatus.NOT_FOUND);

        licence = licenceOptional.get();
        licence.setStatut(2);
        licence = licenceRepository.save(licence);
        LicenceReturnRequest newRequestReturn = new LicenceReturnRequest();
        newRequestReturn.setAgence(licence.getPoste().getAgence().getNom());
        newRequestReturn.setModule(licence.getModule().getLibelleModule());
        newRequestReturn.setInstitution(licence.getPoste().getAgence().getInstitution().getNomInst());

        return newRequestReturn;
    }
}
