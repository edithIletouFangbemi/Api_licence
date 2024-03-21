package com.example.Api_version.repositories;

import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.Institution;
import com.example.Api_version.entities.Module;
import com.example.Api_version.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Integer> {
  Optional<Agence> findByCodeAgenceIgnoreCaseAndInstitution(String codeAgence, Institution institution);
  Optional<Agence> findByNomIgnoreCaseAndInstitution(String nom, Institution institution);
  Optional<Agence> findByCodeAgenceIgnoreCase(String codeAgence);

  Optional<Agence> findByIdAndInstitution(int id, Institution institution);
  @Query(value = "select DISTINCT p.codeproduit, p.nom from produit p JOIN contrat_institution c ON \n" +
          "c.produit_codeproduit = p.codeproduit JOIN institution i ON c.institution_codeinst = i.codeinst\n" +
          "where p.statut = 1 AND i.statut = 1 AND c.statut = 1 AND i.codeinst = :codeinst", nativeQuery = true)
  List<Object[]> products(@Param("codeinst") String codeinst) ;

  List<Agence> findAllByStatut(int statut);
  @Query(value = "SELECT p.* from produit p JOIN contratproduit pt ON p.codeproduit = pt.produit\n" +
          "JOIN contrat_institution c ON pt.contratinstitution = c.codecontrat\n" +
          "WHERE pt.statut = 1 AND c.statut = 1 AND p.statut = 1 AND c.codecontrat = :codeContrat AND \n" +
          " c.institution_codeinst = :codeInst ;", nativeQuery = true)
  List<Object[]> Produits(@Param("codeInst") String codeInst, @Param("codeContrat") String codeContrat);
  @Query(value = "select m.* from module m JOIN produit p ON m.produit_codeproduit = p.codeproduit\n" +
          "WHERE m.statut = 1 AND p.statut = 1 AND p.codeproduit = :codeProduit AND m.type_module = 'standard';", nativeQuery = true)
  List<Object[]> modules(@Param("codeProduit") String codeProduit);
  Optional<Agence> findByInstitutionAndCodeAgenceAndStatut(Institution institution, String codeAgence, int statut);

  Optional<Agence> findByIdAndInstitutionAndStatut(int id,Institution institution, int statut);
  List<Agence> findAllByInstitutionAndStatut(Institution institution, int statut);



  @Query(value = "select DISTINCT m.* from module m\n" +
          "Join produit p ON m.produit_codeproduit = p.codeproduit\n" +
          "Join contrat_institution c ON c.produit_codeproduit = p.codeproduit\n" +
          "JOIN institution i On c.institution_codeinst = i.codeinst\n" +
          "JOIn agence a ON a.institution_codeinst = i.codeinst\n" +
          "Where p.codeproduit = :codeproduit AND m.type_module = 'additionnel'\n" +
          "AND m.codemodule not IN (select module_codemodule from detailcontrat where statut = 1 AND agence_codeagence = :codeagence )\n" +
          "          \n" +
          ";", nativeQuery = true)
  List<Object[]> moduleProduit(@Param("codeagence") String codeagence, @Param("codeproduit") String codeproduit);


}



