package com.example.Api_version.utils;
import java.util.List;
import java.util.Random;

public class CodeGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    static StringBuilder codeBuilder = new StringBuilder();

   public static String generateCode(String nom, String identifiant){
       codeBuilder = new StringBuilder();
       codeBuilder.append(nom.substring(0,3));
       codeBuilder.append(identifiant.substring(0,3));
       codeBuilder = new StringBuilder(codeBuilder.substring(0, 4).toString());
       Random random = new Random();
       for (int i = 0; i < 2; i++) {
           int index = random.nextInt(ALPHABET.length());
           codeBuilder.append(ALPHABET.charAt(index));
       }

       return codeBuilder.toString();
   }

   public static String passwordCode(String nom, String prenom){
       codeBuilder = new StringBuilder();
       codeBuilder.append(nom.substring(0,2));
       codeBuilder.append(prenom.substring(0,2));

       codeBuilder = new StringBuilder(codeBuilder.substring(0, 4).toString());
       Random random = new Random();
       for (int i = 0; i < 2; i++) {
           int index = random.nextInt(ALPHABET.length());
           codeBuilder.append(ALPHABET.charAt(index));
       }

       return codeBuilder.toString();
   }

    public static String codeUser(String nom, String prenom){
        codeBuilder = new StringBuilder();
        codeBuilder.append(nom.substring(0,2));
        codeBuilder.append(prenom.substring(0,2));

        codeBuilder = new StringBuilder(codeBuilder.substring(0, 3).toString());

        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }

        return codeBuilder.toString();
    }

    public static String codeHabilitation(String libelle){
        codeBuilder = new StringBuilder();
        codeBuilder.append(libelle.substring(0,3));
        Random random = new Random();

        codeBuilder = new StringBuilder(codeBuilder.substring(0,3).toString());

        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }
        return codeBuilder.toString();
    }

    public static String codeProfil(String libelle){
        codeBuilder = new StringBuilder();
        codeBuilder.append(libelle.substring(0,3));
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }
        return codeBuilder.toString();
    }

    public static String libelleParametre(List<String> parametres){
       var tailleCode =12;
        codeBuilder = new StringBuilder();
       parametres.forEach(e ->{codeBuilder.append(e.substring(0,2));});
       // codeBuilder = new StringBuilder(codeBuilder.substring(0,4).toString());
        Random random = new Random();
        for (int i = 0; i < (tailleCode - 2 * parametres.size()); i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }
        return codeBuilder.toString();
    }

    public static String codeParametre(String libelle){
        codeBuilder = new StringBuilder();
       codeBuilder.append(libelle.substring(0,3));
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }
        return codeBuilder.toString();
    }

    public static String codeContrat(String libelle, String typeContrat){
        codeBuilder = new StringBuilder();
        codeBuilder.append(libelle.substring(0,2));
        codeBuilder.append(typeContrat.substring(0,2));
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }

        return codeBuilder.toString();
    }

    public static String codeDetailContrat(String nomAgence){
        codeBuilder = new StringBuilder();
        codeBuilder.append(nomAgence.substring(0,3));
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }
        return codeBuilder.toString();
    }

    public static String codeLicenceserveur(String codeInst, String codeAgence){
        codeBuilder = new StringBuilder();
        codeBuilder.append(codeInst.substring(0,2));
        codeBuilder.append(codeAgence.substring(0,2));
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }
        return codeBuilder.toString();
    }

    public static String keyBuilder(String institutionCode,String agenceCode){
        StringBuilder keyBuild = new StringBuilder();
        keyBuild.append(institutionCode.substring(0,3));
        keyBuild.append(agenceCode.substring(0,3));

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(ALPHABET.length());
            keyBuild.append(ALPHABET.charAt(index));
        }

        return keyBuild.toString();
    }

    public static String generateKey(String data, String nomModule){
       codeBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 14; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }

        codeBuilder.append(nomModule.substring(0,2));

        return codeBuilder.toString();
    }

    public static String filenameEnd(){
        codeBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }

        return codeBuilder.toString();
    }

    public static String keyReturn(){
        codeBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(ALPHABET.length());
            codeBuilder.append(ALPHABET.charAt(index));
        }

        return codeBuilder.toString();
    }




}
