package com.example.Api_version.repositories;

import com.example.Api_version.entities.Produit;
import com.example.Api_version.request.ProduitReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Integer> {
    Optional<Produit> findByCodeProduitAndStatut(String codeProduit, int statut);
    Optional<Produit> findByCodeProduitIgnoreCase(String codeProduit);
    Optional <Produit> findByNomIgnoreCase(String nom);
    Optional<Produit> findByIdAndStatut(int id, int statut);
    List<Produit> findAllByStatut(int statut);

    @Query(value = "SELECT DISTINCT p.code_produit, p.nom, p.description, p.statut as statut,\n" +
            "               COALESCE(COUNT(CASE WHEN m.type_module = 'standard' THEN m.id END), 0) as nbrModuleStandard ,\n" +
            "               COALESCE(COUNT(CASE WHEN m.type_module = 'additionnel' THEN m.id END), 0) as nbrModuleAdditionnel, p.id, p.date_creation\n" +
            "FROM produit p \n" +
            "LEFT JOIN module m ON m.produit_id = p.id\n" +
            "WHERE p.statut = 1 AND (m.statut = 1 OR m.statut IS NULL)\n" +
            "GROUP BY p.nom, p.description, p.statut, p.id,p.code_produit, p.date_creation " +
            " ORDER BY p.date_creation DESC;",

            nativeQuery = true)

    List<Object[]> findAllProduitAndCountModule();
}
