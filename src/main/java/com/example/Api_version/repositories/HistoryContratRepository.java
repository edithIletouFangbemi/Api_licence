package com.example.Api_version.repositories;

import com.example.Api_version.entities.HistoContrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryContratRepository extends JpaRepository<HistoContrat, Integer> {

}
