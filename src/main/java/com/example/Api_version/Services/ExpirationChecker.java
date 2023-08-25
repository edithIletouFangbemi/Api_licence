package com.example.Api_version.Services;

import com.example.Api_version.entities.Institution;
import com.example.Api_version.repositories.InstitutionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExpirationChecker {

    private InstitutionRepository institutionRepository;

    public ExpirationChecker(InstitutionRepository institutionRepository){
        this.institutionRepository = institutionRepository;
    }

    @Scheduled(fixedDelay = 3600000)
    public void check(){

    }
}
