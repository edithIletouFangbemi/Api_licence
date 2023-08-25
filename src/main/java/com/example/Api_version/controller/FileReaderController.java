package com.example.Api_version.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("fileReader/")
public class FileReaderController {
    @PostMapping("upload")
    public String readFile(@RequestParam("file")MultipartFile file){
        try{
            String content = new String(file.getBytes());
            return "Contenu du fichier : " + content;
        }
        catch (IOException e){
            e.printStackTrace();
            return "Erreur lors de la lecture du fichier";
        }
    }
}
