package com.example.Api_version.repositories;

import com.example.Api_version.entities.Module;
import com.example.Api_version.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, String> {
    Optional<Module> findByCodeModuleAndStatut(String code, int statut);
    Optional<Module> findByLibelleModuleAndProduit(String libelleModule, Produit produit);
    Optional<Module> findByCodeModuleAndProduitAndStatut(String code, Produit produit, int statut);
    List<Module> findAllByStatut(int statut);

    Optional<Module> findByProduitAndTypeModuleAndStatut(Produit produit,String typeModule, int statut);
    @Query(value = "SELECT m.* " +
            "FROM module m JOIN produit p " +
            "ON m.produit_codeproduit = p.codeproduit ",nativeQuery = true)
    List<Module> selectModules(@Param("codeProduit") String codeProduit, @Param("codeAgence") String codeAgence);
    Optional<Module> findByCodeModule(String codeModule);
    List<Module> findAllByProduitAndTypeModuleAndStatut(Produit produit, String type_module, int statut);
    List<Module> findAllByProduitAndStatut(Produit produit, int statut);
}
