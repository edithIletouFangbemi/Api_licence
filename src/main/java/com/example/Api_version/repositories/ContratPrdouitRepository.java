package com.example.Api_version.repositories;

import com.example.Api_version.entities.ContratProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratPrdouitRepository extends JpaRepository<ContratProduit,Long> {
    List<ContratProduit> findAllByContratInstitutionAndStatut(String contratInstitution, int statut);

}
