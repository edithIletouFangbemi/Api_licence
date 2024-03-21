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
public interface ParametreRepository extends JpaRepository<Parametre, Integer> {
    Optional<Parametre> findByLibelleAndDateDebut(String libelle, Date date_debut);
    List<Parametre> findAllByStatut(int statut);
    Optional<Parametre> findByIdAndStatut(int id, int statut);
    Optional<Parametre> findByStatut(int statut);
    @Query(value = "SELECT * FROM parametre order by statut ASC", nativeQuery = true)
    List<Parametre> findAllParametre();
    Optional<Parametre> findByCodeParametreAndStatut(String codeParametre, int statut);
    @Query("select p from Parametre p" +
            " where p.statut = 1 AND :dateForCheck BETWEEN p.dateDebut AND p.dateFin")
    Optional<Parametre> getParametreByDate(@Param("dateForCheck") Date dateForCheck);
    @Query( " select count(p.id) from Parametre p" +
            " WHERE (p.dateDebut BETWEEN :dateDebut AND :dateFin ) OR "+
            " ( :dateDebut BETWEEN p.dateDebut AND p.dateFin ) OR "+
            " ( :dateDebut BETWEEN p.dateDebut AND p.dateFin ) AND p.statut = 1 "
           )
    Long countNbrParametreOverlapping(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);

    @Query(" select count(p.id) from Parametre p " +
            " WHERE p.id != :id AND ((p.dateDebut BETWEEN :dateDebut AND :dateFin ) OR \n" +
            "             ( :dateDebut BETWEEN p.dateDebut AND p.dateFin ) OR \n" +
            "             ( :dateDebut BETWEEN p.dateDebut AND p.dateFin ) AND p.statut = 1)"

            )
    Long countNbrParametreOverlappingAndCheckId(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin, @Param("id") int id);

}
