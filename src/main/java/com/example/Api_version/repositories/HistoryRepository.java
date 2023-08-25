package com.example.Api_version.repositories;

import com.example.Api_version.entities.Historique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<Historique, Integer> {
}
