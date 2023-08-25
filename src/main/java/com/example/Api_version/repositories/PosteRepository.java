package com.example.Api_version.repositories;

import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PosteRepository extends JpaRepository<Poste,String> {
    Optional<Poste> findByAdresseIpAndAdresseMacAndIdMachineAndIdDisque(
            String adresseIp,
            String adresseMac,
            String idMachine,
            String idDisque
    );

    List<Poste> findAllByAgenceAndStatut(Agence agence, int statut);
    Optional<Poste> findByCodePosteAndStatut(String codePoste, int statut);
    Optional<Poste> findByAgenceAndCodePosteAndStatut(Agence agence,String codePoste , int statut);
}
