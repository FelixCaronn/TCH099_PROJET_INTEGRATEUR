package com.example.tch099_projet_integrateur.info_user;

public class RecuLogin {

    private int code;
    private String reponse;
    private String nom;
    private int id;

    public RecuLogin() {
        code = 0;
        reponse ="";
        nom = "";

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCode() {
        return code;
    }

    public String getReponse() {
        return reponse;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
}
