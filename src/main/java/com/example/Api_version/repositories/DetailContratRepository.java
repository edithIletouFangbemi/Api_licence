package com.example.Api_version.repositories;

import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.DetailContrat;
import com.example.Api_version.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetailContratRepository extends JpaRepository<DetailContrat,String> {
    Optional<DetailContrat> findByCodeDetailContrat(String codeDetailContrat);
    Optional<DetailContrat> findByCodeDetailContratAndStatut(String codeDetailContrat, int statut);
    List<DetailContrat> findByAgenceAndStatut(Agence agence, int statut);
    Optional<DetailContrat> findByIdAndStatut(int id, int statut);
    List<DetailContrat> findAllByStatut(int statut);
    Optional<DetailContrat> findByAgenceAndModuleAndStatut(Agence agence, Module module, int statut);
}
