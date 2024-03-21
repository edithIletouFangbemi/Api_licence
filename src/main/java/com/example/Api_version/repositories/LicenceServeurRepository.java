package com.example.Api_version.repositories;

import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.LicenceServeur;
import com.example.Api_version.entities.Module;
import com.example.Api_version.entities.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LicenceServeurRepository extends JpaRepository<LicenceServeur, Integer> {
    Optional<LicenceServeur> findByAgenceAndStatut(Agence agence, int statut);
    List<LicenceServeur> findAllByStatut(int statut);

    Optional<LicenceServeur> findByPosteAndModuleAndAgenceAndStatut(Poste poste, Module module,Agence agence,int statut);
    Optional<LicenceServeur> findByCodeAndStatut(String code, int statut);

    @Query(value = "select DIStinct l.code as code, l.datecreation as datecreation, l.statut as statut,\n" +
            "             a.codeagence as codeagence, a.nom as nom, i.codeinst as codeinst, i.nominst as nominst, i.typearchitecture, count(l.code)\n" +
            "            from licenceserveur l JOIN agence a on l.agence_codeagence = a.codeagence JOIN institution i on\n" +
            "            a.institution_codeinst = i.codeinst \n" +
            "            where i.statut = 1 AND a.statut = 1  AND l.statut = 1\n" +
            "\t\t\t GROUP BY  l.code , a.codeagence, i.codeinst\n" +
            "\t\t\t ORDER BY i.codeinst;", nativeQuery = true)
    List<Object[]> returnResult();

    @Query(value = "select DIStinct l.code as code, l.datecreation as datecreation, l.statut as statut,\n" +
            "             a.codeagence as codeagence, a.nom as nom, i.codeinst, i.nomInst, m.libellemodule\n" +
            "            from licenceserveur l JOIN agence a on l.agence_codeagence = a.codeagence JOIN institution i on\n" +
            "            a.institution_codeinst = i.codeinst  JOIN detailcontrat d on d.agence_codeagence = a.codeagence JOIn module m on" +
            " m.codemodule = d.module_codemodule \n" +
            "            where m.codemodule = l.module_codemodule AND i.statut = 1 AND a.statut = 1  AND l.statut = 1 AND i.codeinst = :codeinst\n" +
            "\t\t\t \n" +
            "\t\t\t ;", nativeQuery = true)
    List<Object[]> licenceAgenceByInstitution(@Param("codeinst")String codeinst);
    /*
    @Query("select * from LicenceServeur l " +
            "where l.statut = 1 AND l.id = :idLicence ")
    LicenceServeur codeForDownload(@Param("idLicence")int idLicence);
    */
    Optional<LicenceServeur> findByIdAndStatut(int id, int statut);
    @Query(value = "SELECT DISTINCT a.codeagence, a.nom " +
            "FROM agence a JOIN institution i ON a.institution_codeinst = i.codeinst  JOIN contrat_institution c  ON i.codeinst = c.institution_codeinst " +
            "WHERE c.statut = 1 AND i.codeinst = :codeInst AND i.statut = 1 AND" +
            " a.codeagence not in (SELECT agence_codeagence from licenceserveur) AND a.statut = 1", nativeQuery = true)
    List<Object[]> listeAgence(@Param("codeInst") String codeInst);

    @Query(value = "SELECT DISTINCT a.codeagence, a.nom \n" +
            "            FROM agence a JOIN institution i ON a.institution_codeinst = i.codeinst  JOIN contrat_institution c  ON i.codeinst = c.institution_codeinst \n" +
            "             JOIN produit p on p.codeproduit = c.produit_codeproduit JOIN module m on m.produit_codeproduit = p.codeproduit join detailcontrat d\n" +
            "             ON d.module_codemodule = m.codemodule \n" +
            "            WHERE d.typecontratmodule='illimite' AND c.statut = 1 AND i.codeinst = :codeInst AND i.statut = 1 AND p.codeproduit = :codeproduit AND\n" +
            "             a.statut = 1 AND m.codemodule not in (select module_codemodule from licenceserveur where statut = 1 and agence_codeagence = a.codeagence)", nativeQuery = true)
    List<Object[]> listeAgenceServeur(@Param("codeInst") String codeInst, @Param("codeproduit") String codeproduit);


    @Query(value = "select i.codeinst, i.nominst, i.adresseinst, i.statut\n" +
            "From institution i JOIN contrat_institution c ON i.codeinst = c.institution_codeinst\n" +
            "JOIN agence a ON c.institution_codeinst = a.institution_codeinst\n" +
            "WHERE i.statut = 1 AND c.statut = 1 AND a.statut=1 AND a.codeagence NOT IN (select agence_codeagence FROM licenceserveur)\n" +
            "GROUP BY i.codeinst, i.nominst, i.adresseinst, i.statut;", nativeQuery = true)
    List<Object[]> institutionLicence();
    @Query(value="select DISTINCT p.codeproduit, p.nom from produit p JOIN contrat_institution c ON c.produit_codeproduit = p.codeproduit\n" +
            "JOIN institution i ON c.institution_codeinst = i.codeinst\n" +
            "JOIN agence a ON a.institution_codeinst = i.codeinst JOIN detailcontrat d ON d.agence_codeagence = a.codeagence\n" +
            "JOIN module m ON m.codemodule in (select module_codemodule from detailcontrat where statut=1 AND typecontratmodule='illimite')\n" +
            "WHERE d.statut = 1 AND c.statut = 1 AND a.statut = 1 AND i.codeinst = :codeinst", nativeQuery = true)
    List<Object[]> listeProduit(@Param("codeinst") String codeinst);
    @Query(value="select DISTINCT m.codemodule, m.libellemodule from module m JOIN produit p ON m.produit_codeproduit = p.codeproduit \n" +
            "                        JOIN contrat_institution c ON p.codeproduit = c.produit_codeproduit\n" +
            "                        JOIN institution i ON c.institution_codeinst = i.codeinst JOIN agence a ON i.codeinst = a.institution_codeinst\n" +
            "                        JOIN detailcontrat d ON a.codeagence = d.agence_codeagence\n" +
            "                        where m.statut = 1 AND p.statut = 1   AND c.statut = 1 AND a.statut = 1 AND d.statut = 1\n" +
            "                        AND m.codemodule = d.module_codemodule\n" +
            "             AND d.typecontratmodule = 'illimite' AND\n" +
            "                        p.codeproduit = :codeproduit AND a.codeagence = :codeagence\n" +
            "                        GROUP BY c.codecontrat, m.codemodule", nativeQuery = true)
    List<Object[]> listemodule(@Param("codeagence") String codeagence, @Param("codeproduit") String codeproduit);

    Optional<LicenceServeur> findByAgenceAndModuleAndStatut(Agence agence, Module module, int statut);
    @Query("select ls.id, ls.agence.nom, ls.poste.idMachine, ls.module.libelleModule, ls.dateCreation, ls.statut from LicenceServeur ls" +
            " where ls.statut = :statut AND ls.dateCreation BETWEEN :dateDebut AND :dateFin " +
            " AND ls.agence.id = :agenceId AND ls.poste.id IN :posteIds AND ls.module.id IN :moduleIds ")
    List<Object[]> situationLicenceServeur(@Param("agenceId") int agenceId,@Param("posteIds") List<Integer> posteIds,@Param("moduleIds") List<Integer> moduleIds
            , @Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin, @Param("statut") int statut);
    @Query(" select ls from LicenceServeur ls where ls.statut = :statut AND ls.id IN :listeIdLicence")
    List<LicenceServeur> findAllByIdAndStatut(@Param("listeIdLicence") List<Integer> listeIdLicence, @Param("statut") int statut);
}
