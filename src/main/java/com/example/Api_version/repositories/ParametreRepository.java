package com.example.Api_version.repositories;

import com.example.Api_version.entities.Parametre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, String> {
    Optional<Parametre> findByLibelleAndDateDebut(String libelle, Date date_debut);
    List<Parametre> findAllByStatut(int statut);
   Optional<Parametre> findByStatut(int statut);
    @Query(value = "SELECT * FROM parametre order by statut ASC", nativeQuery = true)
    List<Parametre> findAllParametre();
    Optional<Parametre> findByCodeParametreAndStatut(String codeParametre, int statut);
}
