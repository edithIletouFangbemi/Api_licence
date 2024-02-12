package com.example.Api_version.repositories;

import com.example.Api_version.entities.Institution;
import com.example.Api_version.request.InstitutionReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Integer> {
 Optional<Institution> findByNomInstIgnoreCase(String nomInst);
 Optional<Institution> findByCodeInst(String code);
 Optional<Institution> findByCodeInstAndStatut(String code, int statut);
 Optional<Institution> findByIdAndStatut(int id, int statut);

 List<Institution> findInstitutionByStatut(int statut);

 @Query(value = "SELECT i.codeinst , i.nominst , i.adresseinst, i.statut ,\n" +
         "       COALESCE(COUNT(a.codeagence), 0), i.datecreation\n, i.typearchitecture, i.id" +
         "FROM institution i\n" +
         "LEFT JOIN agence a ON i.codeinst = a.institution_codeinst\n" +
         "WHERE i.statut = 1 AND (a.statut = 1 OR a.statut IS NULL)\n" +
         "GROUP BY i.codeinst, i.nominst, i.adresseinst, i.statut ORDER BY i.datecreation DESC;", nativeQuery = true)
 List<Object[]> findAllByStatut(@Param("statut") int statut);

 @Query(value = "select i.nominst, COALESCE(count(a.codeagence),0) from institution i\n" +
         "JOIN agence a ON i.codeinst = a.institution_codeinst\n" +
         "where i.statut = 1 AND a.statut = 1\n" +
         "GROUP By i.codeinst;", nativeQuery = true)
 List<Object[]> countAgence();
 @Query(value = "select DISTINCT i.nominst, COALESCE(COUNT(p.codeproduit), 0) from institution i JOIN contrat_institution c\n" +
         "ON c.institution_codeinst = i.codeinst JOIN produit p ON c.produit_codeproduit = p.codeproduit\n" +
         "group by  i.codeinst", nativeQuery = true)
 List<Object[]> countWithProduit();
}
