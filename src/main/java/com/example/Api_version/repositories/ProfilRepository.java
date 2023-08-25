package com.example.Api_version.repositories;

import com.example.Api_version.entities.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, String> {
    Optional<Profil> findByLibelle(String libelle);
    Optional<Profil> findByCodeProfil(String codeProfil);
    Optional<Profil> findByCodeProfilAndStatut(String code, int statut);

    List<Profil> findAllByStatut(int statut);
    @Query(value="select p.* ,COUNT(h.codehabilitation) as nbrhabilitation\n" +
            "FROM profil p JOIN profil_habilitations ph ON p.codeprofil = ph.profil_codeprofil\n" +
            "JOIN habilitation h ON ph.habilitations_codehabilitation = h.codehabilitation\n" +
            "WHERE p.statut = 1 AND h.statut = 1\n" +
            "GROUP BY h.codehabilitation, p.codeprofil;", nativeQuery = true)
    List<Object[]> AllProfil();
}
