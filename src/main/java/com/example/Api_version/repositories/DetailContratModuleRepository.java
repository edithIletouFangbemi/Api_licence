package com.example.Api_version.repositories;

import com.example.Api_version.entities.DetailContratModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailContratModuleRepository extends JpaRepository<DetailContratModule,Integer> {
    Optional<DetailContratModule> findByCodeAgenceAndCodeDetailAndCodeModuleAndStatut(String codeAgence,
                                                                                      String codeDetail,
                                                                                      String codeModule,
                                                                                      int statut);
}
