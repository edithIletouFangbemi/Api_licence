package com.example.Api_version.repositories;

import com.example.Api_version.entities.Produit;
import com.example.Api_version.request.ProduitReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, String> {
    Optional<Produit> findByCodeProduitAndStatut(String codeProduit, int statut);
    Optional<Produit> findByCodeProduit(String codeProduit);
    Optional <Produit> findByNom(String nom);
    List<Produit> findAllByStatut(int statut);

    @Query(value = "SELECT DISTINCT p.codeproduit, p.nom, p.description, p.statut as statut,\n" +
            "               COALESCE(COUNT(CASE WHEN m.type_module = 'standard' THEN m.codeModule END), 0) as nbrModuleStandard ,\n" +
            "               COALESCE(COUNT(CASE WHEN m.type_module = 'additionnel' THEN m.codeModule END), 0) as nbrModuleAdditionnel, p.datecreation\n" +
            "FROM produit p \n" +
            "LEFT JOIN module m ON m.produit_codeproduit = p.codeproduit\n" +
            "WHERE p.statut = 1 AND (m.statut = 1 OR m.statut IS NULL)\n" +
            "GROUP BY p.nom, p.description, p.statut, p.codeproduit" +
            " ORDER BY p.datecreation DESC;", nativeQuery = true)

    List<Object[]> findAllProduitAndCountModule();
}
