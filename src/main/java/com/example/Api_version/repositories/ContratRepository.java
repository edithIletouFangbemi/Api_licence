package com.example.Api_version.repositories;

import com.example.Api_version.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContratRepository extends JpaRepository<Contrat_Institution, String> {
    Optional<Contrat_Institution> findByInstitution(Institution institution);

    Optional<Contrat_Institution> findByInstitutionAndProduit(Institution institution, Produit produit);
    List<Contrat_Institution> findAllByStatut(int statut);

    @Query(value="select i.codeinst, i.nominst, i.typearchitecture, COUNT(c.produit_codeProduit) from contrat_institution c JOIN \n" +
            "institution i On c.institution_codeinst = i.codeinst\n" +
            " where i.statut =1 AND c.statut = 1\n" +
            " Group by i.codeinst", nativeQuery = true)
    List<Object[]> listeReturn();

    List<Contrat_Institution> findAllByInstitutionAndStatut(Institution institution, int statut);

    Optional<Contrat_Institution> findByProduitAndCodeContratAndStatut(Produit produit, String codeContrat, int statut);
    Optional<Contrat_Institution> findByInstitutionAndProduitAndStatut(Institution institution,
                                         Produit produit,
                                         int statut
                                                                       );

    @Query(value = "SELECT i.codeinst, i.nominst, i.adresseinst, i.statut" +
            " FROM INSTITUTION i " +
            "WHERE i.statut = 1 ", nativeQuery = true)
    List<Object[]> listerInst();

    @Query(value ="SELECT DISTINCT institution_codeInst FROM CONTRAT_INSTITUTION  WHERE statut = 1",nativeQuery = true)
    List<String> findAllInstitution();

    @Query(value = "SELECT DISTINCT m.codemodule AS codemodule, m.libellemodule AS libellemodule, \n" +
            "            m.description AS description, m.statut AS statut, m.produit_codeproduit AS codeproduit,\n" +
            "            m.datecreation AS datecreation, m.type_module AS type_module\n" +
            "FROM MODULE m \n" +
            "JOIN PRODUIT p ON m.produit_codeproduit = p.codeproduit\n" +
            "WHERE p.codeProduit = :codeProduit \n" +
            "AND m.statut = 1", nativeQuery = true)
    List<Object[]> findModuleByProduit(@Param("codeProduit") String codeProduit);
    Optional<Contrat_Institution> findByCodeContrat(String code);
    Optional<Contrat_Institution> findByInstitutionAndStatut(Institution institution, int statut);

    @Query(value = "select produit_codeproduit from contrat_institution " +
            "where institution_codeinst = :codeinst AND statut = 1;", nativeQuery = true)
    List<Object[]> listeProduit(@Param("codeinst") String codeinst);
    @Query(value = "select d.* from detailcontrat d " +
            "where d.agence_codeagence = :codeagence AND d.statut = 1;", nativeQuery = true)
    List<Object[]> unDetail(@Param("codeagence") String codeagence);
    @Query(value = "select c.*, i.*, p.* from contrat_institution c \n" +
            "JOIN institution i ON c.institution_codeinst = i.codeinst\n" +
            "JOIN contratproduit cp ON c.codecontrat = cp.contratinstitution\n" +
            "JOIN produit p ON cp.produit = p.codeproduit\n" +
            "where c.statut = 1 AND c.statut = 1 AND p.statut = 1" +
            " AND c.codecontrat = :codecontrat \n" +
            "GROUP BY c.codecontrat, i.codeinst, p.codeproduit;", nativeQuery = true)
    List<Object[]> detailContrat(@Param("codecontrat") String codecontrat);
    @Query(value = "select m.codemodule, m.libellemodule, COALESCE(COUNT(CASE WHEN l.statut = 0 THEN l.codelicence END), 0) as nbrLicenceEnAttente,\n" +
            "COALESCE(COUNT(CASE WHEN l.statut = 1 THEN l.codelicence END), 0) as nbrLicenceActive,\n" +
            "COALESCE(COUNT(CASE WHEN l.statut = 2 THEN l.codelicence END), 0) as nbrLicenceInactive\n" +
            "from module m JOIN licence l ON m.codemodule = l.module_codemodule JOIN poste pt ON \n" +
            "l.poste_codeposte = pt.codeposte JOIN agence a ON pt.agence_codeagence = a.codeagence\n" +
            "JOIN institution i ON a.institution_codeinst = i.codeinst\n" +
            "JOIN contrat_institution c ON i.codeinst = c.institution_codeinst \n" +
            "JOIN contratproduit cp ON c.codecontrat = cp.contratinstitution\n" +
            "JOIN produit p ON cp.produit = p.codeproduit" +
            " WHERE m.statut = 1 AND a.codeagence = :codeagence AND p.codeproduit = :codeproduit\n" +
            "Group by m.codemodule\n" +
            ";", nativeQuery = true)
    List<Object[]> moduleDetail(@Param("codeagence") String codeagence, @Param("codeproduit") String codeproduit);
    @Query(value = "select \n" +
            "COALESCE(COUNT(CASE WHEN pst.statut = 1 THEN pst.codeposte END), 0) as nbrPoste\n" +
            "from poste pst join agence a ON pst.agence_codeagence = a.codeagence\n" +
            "JOIN institution i On a.institution_codeinst = i.codeinst\n" +
            "Join contrat_institution c On i.codeinst = c.institution_codeinst\n" +
            "JOIN contratproduit cp on c.codecontrat = cp.contratinstitution\n" +
            "Join produit p on cp.produit = p.codeproduit\n" +
            "where a.codeagence= :codeagence And p.codeproduit = :codeproduit\n" +
            "Group by pst.codeposte, a.codeagence, p.codeproduit\n" +
            ";", nativeQuery = true)
    List<Object[]> nbrPosteDetail(@Param("codeagence") String codeagence, @Param("codeproduit") String codeproduit);

    @Query(value="select \n" +
            "COALESCE(COUNT(CASE WHEN pst.statut = 1 THEN pst.codeposte END), 0) as nbrPoste\n" +
            "from poste pst join licence l ON pst.codeposte = l.poste_codeposte Join\n" +
            "agence a ON pst.agence_codeagence = a.codeagence\n" +
            "JOIN institution i On a.institution_codeinst = i.codeinst\n" +
            "Join contrat_institution c On i.codeinst = c.institution_codeinst\n" +
            "JOIN contratproduit cp on c.codecontrat = cp.contratinstitution\n" +
            "Join produit p on cp.produit = p.codeproduit\n" +
            "where a.codeagence= :codeagence AND l.statut = 1\n" +
            "Group by pst.codeposte, a.codeagence, p.codeproduit\n" +
            ";", nativeQuery = true)
    List<Object[]> posteActive(@Param("codeagence") String codeagence);

}
