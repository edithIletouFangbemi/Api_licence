package com.example.Api_version.repositories;

import com.example.Api_version.entities.ParametreDeVieLicence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParametreDeVieRepository extends JpaRepository<ParametreDeVieLicence, Integer> {
    Optional<ParametreDeVieLicence> findByIdAndStatut(int id, int statut);
    List<ParametreDeVieLicence> findAllByStatut(int statut);

    Optional<ParametreDeVieLicence> findByStatutAndTypeLicence(int statut, String typelicence);

    @Query(value="Select * from parametredevielicence order by statut ASC", nativeQuery = true)
    List<Object[]> findAllParams();
}
