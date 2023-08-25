package com.example.Api_version.repositories;

import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.LicenceServeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LicenceServeurRepository extends JpaRepository<LicenceServeur,String> {
    Optional<LicenceServeur> findByAgenceAndStatut(Agence agence, int statut);
    List<LicenceServeur> findAllByStatut(int statut);
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
            "             a.codeagence as codeagence, a.nom as nom, i.codeinst, i.nomInst\n" +
            "            from licenceserveur l JOIN agence a on l.agence_codeagence = a.codeagence JOIN institution i on\n" +
            "            a.institution_codeinst = i.codeinst \n" +
            "            where i.statut = 1 AND a.statut = 1  AND l.statut = 1 AND i.codeinst = :codeinst\n" +
            "\t\t\t GROUP BY  l.code , a.codeagence, i.codeinst\n" +
            "\t\t\t ;", nativeQuery = true)
    List<Object[]> licenceAgenceByInstitution(@Param("codeinst")String codeinst);

    @Query(value = "select DIStinct l.libelle , a.code, i.code\n" +
            "             \n" +
            "            from licenceserveur l JOIN agence a on l.agence_codeagence = a.codeagence JOIN institution i on\n" +
            "            a.institution_codeinst = i.codeinst \n" +
            "            where i.statut = 1 AND a.statut = 1  AND l.statut = 1 AND a.statut = 1" +
            " AND l.code = :codelicence \n" +
            "\t\t\t GROUP BY  l.code , a.codeagence, i.codeinst\n" +
            "\t\t\t ;", nativeQuery = true)
    List<Object[]> codeForDownload(@Param("codelicence")String codelicence);
    @Query(value = "SELECT DISTINCT a.codeagence, a.nom " +
            "FROM agence a JOIN institution i ON a.institution_codeinst = i.codeinst  JOIN contrat_institution c  ON i.codeinst = c.institution_codeinst " +
            "WHERE c.statut = 1 AND i.codeinst = :codeInst AND i.statut = 1 AND" +
            " a.codeagence not in (SELECT agence_codeagence from licenceserveur) AND a.statut = 1", nativeQuery = true)
    List<Object[]> listeAgence(@Param("codeInst") String codeInst);

    @Query(value = "select i.codeinst, i.nominst, i.adresseinst, i.statut\n" +
            "From institution i JOIN contrat_institution c ON i.codeinst = c.institution_codeinst\n" +
            "JOIN agence a ON c.institution_codeinst = a.institution_codeinst\n" +
            "WHERE i.statut = 1 AND c.statut = 1 AND a.statut=1 AND a.codeagence NOT IN (select agence_codeagence FROM licenceserveur)\n" +
            "GROUP BY i.codeinst, i.nominst, i.adresseinst, i.statut;", nativeQuery = true)
    List<Object[]> institutionLicence();
}
