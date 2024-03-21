package com.example.Api_version.repositories;

import com.example.Api_version.entities.*;
import com.example.Api_version.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LicenceRepository extends JpaRepository<Licence, Integer> {
    @Query(value = "select i.* from institution i JOIN agence a ON i.codeinst = a.institution_codeinst JOIN contrat_institution c On a.institution_codeinst = c.institution_codeinst\n" +
            "  WHERE i.statut = 1 AND a.statut = 1 AND c.statut = 1 AND  \n" +
            " a.codeagence in (select DISTINCT agence_codeagence from detailcontrat where statut = 1)\n" +
            " GROUP BY i.codeinst\n" +
            " ;", nativeQuery = true)
    List<Object[]> listeInstitution();

    Optional<Licence> findByIdAndStatut(int id, int statut);
    @Query(value = "select DISTINCT a.* from agence a JOIN institution i ON a.institution_codeinst = i.codeinst \n" +
            "                                     JOIN contrat_institution c ON i.codeinst = c.institution_codeinst\n" +
            " JOIN produit p ON c.produit_codeproduit = p.codeproduit\n" +
            " JOIN module m ON p.codeproduit = m.produit_codeproduit\n" +
            " JOIN detailcontrat d\n" +
            "                       ON m.codemodule = d.module_codemodule \n" +
            "                         where a.statut = 1 AND i.statut = 1 AND c.statut = 1 AND" +

            " d.typecontratmodule='limite' AND " +
            "                             p.codeproduit = :codeproduit AND i.codeinst = :codeinst AND a.codeagence in \n" +
            "                         (select DISTINCT agence_codeagence from detailcontrat where statut = 1);", nativeQuery = true)
    List<Object[]> listeAgence(@Param("codeinst") String codeinst, @Param("codeproduit") String codeproduit);

    @Query(value = " select DISTINCT p.* from institution i JOIN contrat_institution c ON i.codeinst = c.institution_codeinst\n" +
            "              JOIN produit p ON\n" +
            "             c.produit_codeproduit = p.codeproduit\n" +
            "             WHERE i.statut = 1 AND c.statut = 1 AND p.statut = 1 " +
            " AND i.codeinst = :codeinst\n" +
            "             GROUP BY c.codecontrat, p.codeproduit;", nativeQuery = true)
    List<Object[]> listeProduit(@Param("codeinst") String codeinst);
    @Query(value = "select DISTINCT m.* from module m JOIN produit p ON m.produit_codeproduit = p.codeproduit \n" +
            "            JOIN contrat_institution c ON p.codeproduit = c.produit_codeproduit\n" +
            "            JOIN institution i ON c.institution_codeinst = i.codeinst JOIN agence a ON i.codeinst = a.institution_codeinst\n" +
            "            JOIN detailcontrat d ON a.codeagence = d.agence_codeagence\n" +
            "            where m.statut = 1 AND p.statut = 1   AND c.statut = 1 AND a.statut = 1 AND d.statut = 1\n" +
            "            AND m.codemodule = d.module_codemodule\n" +
            " AND d.typecontratmodule = 'limite'" +
            "            AND a.codeagence = :codeagence AND p.codeproduit = :codeproduit\n" +
            "            GROUP BY c.codecontrat, m.codemodule"
            , nativeQuery = true)
    List<Object[]> modules(@Param("codeagence") String codeagence, @Param("codeproduit") String codeproduit);
    @Query(value = "select COUNT(pt.codeposte) as nbrPoste \n" +
            "from poste pt JOIN agence a ON pt.agence_codeagence = a.codeagence\n" +
            "JOIN institution i ON a.institution_codeinst = i.codeinst\n" +
            "where pt.statut = 1 AND a.statut = 1 AND i.statut = 1 AND i.codeinst = :codeinst\n" +
            "group by pt.codeposte", nativeQuery = true)
    List<Object[]> countLicence(@Param("codeinst") String codeinst);

    @Query(value = "select COUNT(pt.codeposte) as nbrPoste \n" +
            " from poste pt JOIN agence a ON pt.agence_codeagence = a.codeagence\n" +
            " JOIN licence l ON pt.codeposte = l.poste_codeposte JOIN module m ON l.module_codemodule = m.codemodule" +
            " where pt.statut = 1 AND a.statut = 1 AND l.statut != 2 AND m.statut = 1 AND a.codeagence = :codeagence" +
            " AND m.codemodule = :codemodule \n" +
            " group by pt.codeposte", nativeQuery = true)
    List<Object[]> countLicenceByModuleByAgence(@Param("codeagence") String codeagence, @Param("codemodule") String codemodule);

    @Query(value = "select COUNT(pt.codeposte) as nbrPoste \n" +
            " from poste pt JOIN agence a ON pt.agence_codeagence = a.codeagence\n " +
            " JOIN institution i ON a.institution_codeinst = i.codeinst" +
            " JOIN licence l ON pt.codeposte = l.poste_codeposte JOIN module m ON l.module_codemodule = m.codemodule" +
            " where pt.statut = 1 AND a.statut = 1 AND l.statut != 2 AND m.statut = 1 " +
            " AND m.codemodule = :codemodule AND i.codeinst = :codeinst " +
            "AND a.codeagence = :codeagence\n" +
            " group by pt.codeposte", nativeQuery = true)
    List<Object[]> countLicenceByModuleByAgence(@Param("codeinst") String codeinst, @Param("codemodule") String codemodule,@Param("codeagence") String codeagence);

    Optional<Licence> findByPosteAndModuleAndStatut(Poste poste, Module module , int statut);

    @Query(value = "select i.codeinst, i.nominst, a.codeagence, a.nom, i.typearchitecture, COUNT( DISTINCT pt.codeposte) nbrPoste\n" +
            "from poste pt JOIN agence a ON pt.agence_codeagence = a.codeagence\n" +
            "JOIN institution i ON a.institution_codeinst = i.codeinst JOIN contrat_institution c \n" +
            "ON c.institution_codeinst = i.codeinst\n" +
            "where i.statut = 1 AND a.statut = 1 AND  c.statut = 1 AND pt.statut = 1 \n" +
            "Group by pt.codeposte, i.codeinst, a.codeagence\n" +
            ";", nativeQuery = true)
    List<Object[]> ListeRecapLicence();
    @Query(value="select DISTINCT i.codeinst, i.nominst,i.typearchitecture,COUNT(c.produit_codeproduit)\n" +
            "            from poste pt JOIN agence a ON pt.agence_codeagence = a.codeagence\n" +
            "            JOIN institution i ON a.institution_codeinst = i.codeinst JOIN contrat_institution c \n" +
            "            ON c.institution_codeinst = i.codeinst\n" +
            "            where i.statut = 1 AND a.statut = 1 AND  c.statut = 1 AND pt.statut = 1\n" +
            "            Group by pt.codeposte, i.codeinst", nativeQuery = true)
    List<Object[]> listeRecapitulatif();

    List<Licence> findAllByStatut(int statut);
    @Query(value = "select a.codeagence, a.nom, COUNT( DISTINCT p.codeproduit) nbrProduit, COUNT( DISTINCT m.codemodule) nbrmodule\n" +
            "            from produit p join module m ON p.codeproduit = m.produit_codeproduit join detailcontrat d\n" +
            "\t\t\ton d.module_codemodule = m.codemodule JOIN \n" +
            "\t\t\tagence a ON d.agence_codeagence = a.codeagence\n" +
            "            JOIN institution i ON a.institution_codeinst = i.codeinst JOIN contrat_institution c \n" +
            "            ON c.institution_codeinst = i.codeinst\n" +
            "            where i.statut = 1 AND a.statut = 1 AND  c.statut = 1 AND d.statut = 1 AND p.statut = 1\n" +
            "\t\t\tAND m.statut = 1 AND i.codeinst = :codeinst\n" +
            "            Group by i.codeinst, a.codeagence", nativeQuery = true)
    List<Object[]> listeRecapAgenceByInst(@Param("codeinst") String codeinst);
    @Query(value="select p.codeproduit,p.nom, m.codemodule , m.libellemodule, pt.codeposte,pt.idmachine, l.codelicence, l.statut\n" +
            "from poste pt\n" +
            "JOIN licence l ON pt.codeposte = l.poste_codeposte join module m \n" +
            "on l.module_codemodule = m.codemodule JOIN produit p ON p.codeproduit = m.produit_codeproduit\n" +
            "JOIN agence a ON a.codeagence = pt.agence_codeagence\n" +
            "where m.statut = 1 AND p.statut = 1 AND a.codeagence = :codeagence", nativeQuery = true)
    List<Object[]> listeRecapPosteLicence(@Param("codeagence") String codeagence);
    List<Licence> findAllByPoste(Poste poste);
    @Query(value="select l.libelle, l.module_codemodule, l.poste_codeposte, l.key from licence l where l.codelicence = :codelicence",nativeQuery = true)
    List<Object[]> getLicence(@Param("codelicence") String codelicence);
    @Query(" select l from Licence l " +
            " where l.statut = 1 AND :idLicence = l.id ")
    Licence downloadLicence(@Param("idLicence") int idLicence);

    @Query("select l.id, l.poste.agence.nom, l.poste.idMachine, l.module.libelleModule, l.dateCreation, l.statut from Licence l " +
            " where l.statut = :statut AND l.dateCreation BETWEEN :dateDebut AND :dateFin " +
            " AND l.poste.agence.id = :agenceId AND l.poste.id IN :posteIds AND l.module.id IN :moduleIds ")
    List<Object[]> situationLicence(@Param("agenceId") int agenceId, @Param("posteIds") List<Integer> posteIds, @Param("moduleIds") List<Integer> moduleIds
            , @Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin , @Param("statut") int statut);

    @Query(" select l from Licence l where l.statut = :statut AND l.id IN :listeIdLicence")
    List<Licence> findAllByIdAndStatut(@Param("listeIdLicence") List<Integer> listeIdLicence, @Param("statut") int statut);
}
