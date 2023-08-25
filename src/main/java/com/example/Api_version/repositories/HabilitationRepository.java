package com.example.Api_version.repositories;

import com.example.Api_version.entities.Habilitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabilitationRepository extends JpaRepository<Habilitation, String> {
    Optional<Habilitation> findByCodeHabilitationAndStatut(String codeHabilitation, int statut);

    Optional<Habilitation> findByLibelle(String libelle);

    List<Habilitation> findAllByStatut(int statut);
}
