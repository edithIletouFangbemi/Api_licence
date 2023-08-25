package com.example.Api_version.repositories;

import com.example.Api_version.entities.Habilitation;
import com.example.Api_version.entities.SousHabilitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SousHabilitationRepository extends JpaRepository<SousHabilitation, Integer> {
    Optional<SousHabilitation> findByIdAndStatut(int id, int statut);
    List<SousHabilitation> findAllByHabilitationAndStatut(Habilitation habilitation, int statut);
    Optional<SousHabilitation> findByLibelleAndHabilitation(String libelle, Habilitation habilitation);
}
