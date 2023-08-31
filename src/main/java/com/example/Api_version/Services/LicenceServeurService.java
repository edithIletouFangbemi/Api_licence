package com.example.Api_version.Services;

import com.example.Api_version.config.AESCryptor;
import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.report.FicheProduitEtat;
import com.example.Api_version.repositories.*;
import com.example.Api_version.request.*;
import com.example.Api_version.utils.CodeGenerator;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.ParameterBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.time.LocalDateTime.now;


@Service
@RequiredArgsConstructor
public class LicenceServeurService {
    private final LicenceServeurRepository licenceServeurRepository;
    private final ContratRepository contratRepository;
    private final ProduitRepository produitRepository;
    private final ModuleRepository moduleRepository;
    private final DetailContratRepository detailContratRepository;
    private final InstitutionService institutionService;
    private  final AgenceService agenceService;
    private String returnKey = "#@£%&é'(-è_çà)='?./§!:;,<*µ¤+=}-";
    private Institution institution;
    private LicenceServeur licence;
    private Contrat_Institution contrat;
    private List< LicenceReturnRequest2 > licences;
    private List<Institution> listeInstitution;
    private Agence agence;


    public List<LicenceReturnRequest2> creer(LicenceServeurRequest request) throws Exception {
        List<LicenceReturnRequest2> responses = new ArrayList<>();
        institution = institutionService.OneInstitution(request.getInstitutionCode());
        if(institution == null) throw new ProduitException("aucune institution avec le code "+request.getInstitutionCode());
        Optional<Produit> produitOptional = produitRepository.findByCodeProduitAndStatut(request.getProduit(), 1);
        if(produitOptional.isEmpty()) throw new ProduitException("Aucun produit avec ces informations...");
        var produit = new Produit();
        produit = produitOptional.get();
        Optional<Contrat_Institution> contratInstitutionOptional = contratRepository.findByInstitutionAndProduitAndStatut(institution,produit,1);
        if(contratInstitutionOptional.isEmpty()) throw new ProduitException("ce produit n'est pas actif pour l'institution "+ institution.getNomInst());
        contrat = contratInstitutionOptional.get();
        agence = agenceService.findAgence(institution, request.getAgenceCode());
        if(agence == null) throw new ProduitException("L'institution "+institution.getNomInst()+
                " n'a aucune agence avec le code "+request.getAgenceCode());

            Produit finalProduit = produit;
                request.getModules().forEach(codeModule->{
            Optional<Module> moduleOptional = moduleRepository.findByCodeModuleAndProduitAndStatut(codeModule, finalProduit,1);
            if(moduleOptional.isEmpty()) throw new ProduitException("le produit "+finalProduit.getNom() +" n'a aucun module avec le code "+ codeModule);
            var module = new Module();
            module = moduleOptional.get();

            var detail = new DetailContrat();
            Optional<DetailContrat> detailContratOptional = detailContratRepository.findByAgenceAndModuleAndStatut(agence, module,1);
            if(detailContratOptional.isEmpty()) throw new ProduitException("le module "+ module.getLibelleModule()+" n'est pas actif pour l'agence "+agence.getNom());
            detail = detailContratOptional.get();
            if(detail.getTypeContratModule().equals("limite")) throw new ProduitException("ce module est activé en illimité il est donc impossible de lui générer une licence serveur");
            Optional<LicenceServeur> licenceServeurOptional = licenceServeurRepository.findByAgenceAndModuleAndStatut(agence, module,1);
            if(licenceServeurOptional.isPresent()) throw new ProduitException("cette agence a deja une licence serveur pour le module "+module.getCodeModule());

                    licence = new LicenceServeur();

                    licence.setCode(CodeGenerator.codeLicenceserveur(
                            request.getAgenceCode(), codeModule));
                    String key = CodeGenerator.keyBuilder(request.getAgenceCode(), codeModule);
                    String data = (codeModule+ request.getAgenceCode()).toString();
                    licence.setAgence(agence);
                    licence.setModule(module);
                    licence.setDateCreation(now());
                    licence.setKey(key);
                    licence.setStatut(1);
                    try {
                        licence.setLibelle(AESCryptor.encrypt(data, key));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    licence = licenceServeurRepository.save(licence);

                    var response = LicenceReturnRequest2.builder()
                            .codeLicence(licence.getCode())
                            .agence(agence)
                            .module(module.getLibelleModule())
                            .statut(licence.getStatut())
                            .institution(institution)
                            .build();

                    responses.add(response);
        });


       // Optional<LicenceServeur> licenceServeurOptional = licenceServeurRepository.findByAgenceAndStatut(agence,1);
       // if(licenceServeurOptional.isPresent()) throw new ProduitException("cette agence a deja une licence serveur");




        return responses;
    }

    public List<LicenceReturnRequest2> listeReturn() {
        licences = new ArrayList<>();
        List<Object[]> results = licenceServeurRepository.returnResult();
        for (Object[] result : results) {
            String code = (String) result[0];
            Date datecreation = (Date) result[1];
            int statut = ((Number) result[2]).intValue();
            String codeagence = (String) result[3];
            String nom = (String) result[4];
            String codeinst = (String) result[5];
            String nominst = (String) result[6];
            String typearchitecture = (String)result[7];
            int nbrLicenceServeur = ((Number) result[8]).intValue();


            var institution = new Institution();
            institution.setCodeInst(codeinst);
            institution.setNomInst(nominst);
            institution.setTypeArchitecture(typearchitecture);

            var agence = new Agence();
            agence.setCodeAgence(codeagence);
            agence.setNom(nom);

            var licenceReturn = new LicenceReturnRequest2();
            licenceReturn.setCodeLicence(code);
            licenceReturn.setInstitution(institution);
            licenceReturn.setAgence(agence);
            licenceReturn.setNbrLicenceServeur(nbrLicenceServeur);
            licenceReturn.setStatut(statut);
            licenceReturn.setDateCreation(licenceReturn.getDateCreation());
            licences.add(licenceReturn);
        }
        return licences;
    }

    public DownloadRequest telecharger(String codelicence) throws Exception {
        Object[] result = licenceServeurRepository.codeForDownload(codelicence);

        String code = (String) result[0];
        String key = (String) result[1];
        String agence = (String) result[2];
        String institution = (String) result[3];
        String module = (String) result[4];

        var response = new DownloadRequest();

        response.setCodelicence(AESCryptor.encrypt(code, returnKey));
        response.setKey(AESCryptor.encrypt(key, returnKey));
        response.setCodemodule(AESCryptor.encrypt(module, returnKey));
        response.setCodeAgence(AESCryptor.encrypt(agence, returnKey));
        response.setCodeinst(AESCryptor.encrypt(institution, returnKey));

        return response;

    }

    public List<LicenceReturnRequest2> licenceByInst(String codeinst){
        licences = new ArrayList<>();
        List<Object[]> results = licenceServeurRepository.licenceAgenceByInstitution(codeinst);
        for (Object[] result : results) {
            String code = (String) result[0];
            Date datecreation = (Date) result[1];
            int statut = ((Number) result[2]).intValue();
            String codeagence = (String) result[3];
            String nom = (String) result[4];
            String codeInst = (String) result[5];
            String nomInst = (String) result[6];
            String module = (String)result[7];

            var agence = new Agence();
            agence.setCodeAgence(codeagence);
            agence.setNom(nom);

            var inst = new Institution();
            inst.setCodeInst(codeInst);
            inst.setNomInst(nomInst);

            var licenceReturn = new LicenceReturnRequest2();
            licenceReturn.setCodeLicence(code);
            licenceReturn.setAgence(agence);
            licenceReturn.setInstitution(inst);
            licenceReturn.setStatut(statut);
            licenceReturn.setModule(module);
            licenceReturn.setDateCreation(licenceReturn.getDateCreation());
            licences.add(licenceReturn);
        }
        return licences;
    }

    public List<Institution> listeDesInstitution() {
        List<Object[]> results = licenceServeurRepository.institutionLicence();
        listeInstitution = new ArrayList<>();

        for (Object[] result : results) {
            String codeinst = (String) result[0];
            String nominst = (String) result[1];
            String adresseinst = (String) result[2];
            int statut = ((Number) result[3]).intValue();

            institution = new Institution();
            institution.setCodeInst(codeinst);
            institution.setNomInst(nominst);
            institution.setAdresseInst(adresseinst);
            institution.setStatut(statut);

            listeInstitution.add(institution);
        }
        return  listeInstitution;
    }

    public List<Agence> listeAgence(String codeInst){
        List<Object[]> results = licenceServeurRepository.listeAgence(codeInst);
        List<Agence> agences = new ArrayList<>();

        for(Object[] result: results){
            String codeagence = (String) result[0];
            String nom = (String) result[1];

            agence = new Agence();
            agence.setNom(nom);
            agence.setCodeAgence(codeagence);

            agences.add(agence);
        }

        return agences;
    }

    public List<Agence> listeAgenceServeur(String codeInst, String codeProduit){
        List<Object[]> results = licenceServeurRepository.listeAgenceServeur(codeInst,codeProduit);
        List<Agence> agences = new ArrayList<>();

        for(Object[] result: results){
            String codeagence = (String) result[0];
            String nom = (String) result[1];

            agence = new Agence();
            agence.setNom(nom);
            agence.setCodeAgence(codeagence);

            agences.add(agence);
        }

        return agences;
    }

    public List<Institution> listeInstitution(){
        List<String> results = contratRepository.findAllInstitution();
        List<Institution> institutions = new ArrayList<>();

        results.forEach(code->{
           List<Agence> agencesList = listeAgence(code);
           if(agencesList.isEmpty()){
               results.remove(code);
           }
        });
        results.forEach(code->{
            institution = institutionService.OneInstitution(code);
            institutions.add(institution);
        });

        return institutions;
    }

    public List<Produit> listeProduit(String codeinst){
        List<Object[]> results = licenceServeurRepository.listeProduit(codeinst);

         List<Produit> produitList = new ArrayList<>();

         for (Object[] result : results){
             String codeproduit = (String)result[0];
             String nomproduit = (String)result[1];

             var produit = new Produit();
             produit.setCodeProduit(codeproduit);
             produit.setNom(nomproduit);

             produitList.add(produit);
         }

         return produitList;
    }

    public List<Module> listeModule(String codeproduit, String codeagence){
        List<Object[]> results = licenceServeurRepository.listemodule(codeagence, codeproduit);
        List<Module> modules = new ArrayList<>();
        for(Object[] result: results){
            String codemodule = (String)result[0];
            String nom = (String)result[1];

            var mod = new Module();

            mod.setCodeModule(codemodule);
            mod.setLibelleModule(nom);

            modules.add(mod);
        }
        return modules;
    }

    public String deactivate(String code){
        Optional<LicenceServeur> licenceServeurOptional = licenceServeurRepository.
                findByCodeAndStatut(code,1);
        if(licenceServeurOptional.isEmpty()) throw new ProduitException("aucune licence serveur avec " +
                "le code "+code);
        licence = licenceServeurOptional.get();
        licence.setStatut(2);
        licenceServeurRepository.save(licence);

        return "désactivée avec succès!!";
    }

    public String activate(String code){
        Optional<LicenceServeur> licenceServeurOptional = licenceServeurRepository.
                findByCodeAndStatut(code,2);
        if(licenceServeurOptional.isEmpty()) throw new ProduitException("aucune licence serveur avec " +
                "le code "+code +" n'a été désactivée");
        licence = licenceServeurOptional.get();
        licence.setStatut(1);
        licenceServeurRepository.save(licence);

        return "dactivée avec succès!!";
    }

    public JasperReportBuilder doJasper(TestRequest request)  {
        Map map = new HashMap();
        FicheProduitEtat nouvelFiche = new FicheProduitEtat();

        List<Test> listeTest = new ArrayList<>();

        listeTest.add(new Test("bibliothèque")) ;
        listeTest.add(new Test("librairie"));
        listeTest.add(new Test("boulangerie"));


       /* Entreprise entpse = null;
        Utilisateur util = null;
        FicheDemandeEtat ficheDemandeEtat = new FicheDemandeEtat();
        if (demandeDto.getIdEntreprise() != null) {
            entpse = entrepriseRepository.findById(demandeDto.getIdEntreprise()).orElse(null);
            if(Objects.nonNull(entpse)){
                demandeDto.setNomEntreprise(entpse.getNom());
            }
        }
        if (demandeDto.getIdUtilisateur() != null){
            util= utilisateurRepository.findById(demandeDto.getIdUtilisateur()).orElse(null);
            if(Objects.nonNull(util)){
                demandeDto.setNomUser(util.getNom());
                demandeDto.setPrenomUser(util.getPrenom());
            }
        }
        demandeDto.getListeArticle().forEach(d->{d.setLibelle(d.getArticle().getLibeller());});
        ficheDemandeEtat.setParameterBuilders(getParameters(demandeDto));
        JasperReportBuilder jasperReportBuilder = ficheDemandeEtat.getRapport(ficheDemandeEtat.getTemplate(), demandeDto.getListeArticle());
        return  jasperReportBuilder;
        */

        nouvelFiche.setParameterBuilder(getParameters(request));

        JasperReportBuilder jasperReportBuilder = nouvelFiche.getRapport(nouvelFiche.getTemplate(), listeTest);
        return  jasperReportBuilder;

    }

    private ParameterBuilder<?>[] getParameters(TestRequest request) {
       /* ParameterBuilder<?> mesParameters[] = new ParameterBuilder<?>[14];
        mesParameters[0] = DynamicReports.parameter("numero", demandeDto.getNumero() != null ? demandeDto.getNumero() : "");
        mesParameters[1] = DynamicReports.parameter("date", new SimpleDateFormat("dd/MM/yyyy").format(demandeDto.getDate()));
        mesParameters[2] = DynamicReports.parameter("dateRdv", new SimpleDateFormat("dd/MM/yyyy").format(demandeDto.getDateRdv()));
        mesParameters[3] = DynamicReports.parameter("client", demandeDto.getNom() != null ? demandeDto.getNom() + " " + demandeDto.getPrenom() : "");
        mesParameters[4] = DynamicReports.parameter("contact", demandeDto.getPhonenumber() != null ? demandeDto.getPhonenumber() : "");
        mesParameters[5] = DynamicReports.parameter("address", demandeDto.getAddress() != null ? demandeDto.getAddress() : "");
        mesParameters[6] = DynamicReports.parameter("remise", demandeDto.getRemiseTotale() != null ? demandeDto.getRemiseTotale() : "");
        mesParameters[7] = DynamicReports.parameter("netapayer", demandeDto.getMontantApresRemise() != null ? demandeDto.getMontantApresRemise() : "");
        mesParameters[8] = DynamicReports.parameter("total", demandeDto.getMontantApresRemise() != null ? (demandeDto.getMontantApresRemise() + demandeDto.getRemiseTotale()) : "");
        mesParameters[9] = DynamicReports.parameter("user", demandeDto.getNomUser() != null ? demandeDto.getNomUser() + " " + demandeDto.getPrenomUser() : "");
        mesParameters[10] = DynamicReports.parameter("entreprise", demandeDto.getNomEntreprise() != null ? demandeDto.getNomEntreprise() : "");
        mesParameters[11] = DynamicReports.parameter("dateImpression", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        mesParameters[12] = DynamicReports.parameter("netapayerlettre", AppConstants.formatInLetter(demandeDto.getMontantApresRemise(), Locale.FRENCH));
        mesParameters[13] = DynamicReports.parameter("locale", Locale.FRENCH);*/

        ParameterBuilder<?> mesParameters[] = new ParameterBuilder<?>[3];
        mesParameters[0] = DynamicReports.parameter("institutionCode", "institutionCode");
        mesParameters[1] = DynamicReports.parameter("agenceCode", "agenceCode");
        mesParameters[2] = DynamicReports.parameter("code", "code");
       // mesParameters[3] = DynamicReports.parameter("key", "key");
       // mesParameters[4] = DynamicReports.parameter("module", "module");
      //  mesParameters[5] = DynamicReports.parameter("codePoste", "licence");
       // mesParameters[6] = DynamicReports.parameter("libelle", "licence");

        return mesParameters;
    }


}
