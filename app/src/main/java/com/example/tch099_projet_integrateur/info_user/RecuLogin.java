package com.example.tch099_projet_integrateur.info_user;

public class RecuLogin {

    private int code;
    private String reponse;

    public RecuLogin() {
        code = 0;
        reponse ="";
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
