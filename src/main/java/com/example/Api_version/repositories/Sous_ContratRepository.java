package com.example.Api_version.repositories;

import com.example.Api_version.entities.Contrat_Institution;
import com.example.Api_version.entities.Produit;
import com.example.Api_version.entities.Sous_Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Sous_ContratRepository extends JpaRepository<Sous_Contrat, Integer> {
    Optional<Sous_Contrat> findByIdAndContratAndProduitAndStatutAndType(int id,Contrat_Institution contrat, Produit produit,
                                                            int statut, int type);
    Optional<Sous_Contrat> findByIdAndStatutAndType(int id, int statut, int type);

    Optional<Sous_Contrat> findByIdAndContratAndStatutAndType(int id, Contrat_Institution contrat, int statut, int type);

    Optional<Sous_Contrat> findByContratAndProduitAndStatut(Contrat_Institution contrat, Produit produit,int statut);

    List<Sous_Contrat> findAllByContratAndStatut(Contrat_Institution contrat, int statut);

}
