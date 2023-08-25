package com.example.Api_version.controller;

import com.example.Api_version.Services.CheckExpiration;
import com.example.Api_version.request.CheckReturn;
import com.example.Api_version.request.LicenceReturnRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("valideLicence/")
public class CheckExpirationController {

    private final CheckExpiration checkExpiration;
    @PostMapping("check")
   // @Scheduled(fixedRate = 86400000)
    public ResponseEntity<CheckReturn> FaireUnCheck(@RequestBody LicenceReturnRequest request) throws Exception {
        return new ResponseEntity<CheckReturn>(checkExpiration.checkExpiration(request), HttpStatus.CREATED);
    }
}
