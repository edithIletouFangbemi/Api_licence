package com.example.Api_version.request;

import com.example.Api_version.entities.Agence;
import com.example.Api_version.entities.Institution;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenceReturnRequest implements Serializable {
    private String institution;
    private String agence;
    private String codeLicence;
    private String key;
    private String module;
    private String codePoste;
    private String statut;
}
