package com.example.Api_version.repositories;

import com.example.Api_version.entities.LicenceModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenceModuleRepository extends JpaRepository<LicenceModule, Integer> {
}
