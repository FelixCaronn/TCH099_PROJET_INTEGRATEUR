package com.example.tch099_projet_integrateur.info_user;

public class Notifications {
    private int id, compteId, transactionId;
    private String titre, dateRecu, contenu;
    private boolean lu,doitRepondreQuestion;
    public Notifications(int id, int compteId, int transactionId, String titre, String contenu, String dateRecu, boolean lu, boolean doitRepondreQuestion){
        this.lu = lu;
        this.compteId = compteId;
        this.contenu = contenu;
        this.dateRecu = dateRecu;
        this.titre = titre;
        this.id = id;
        this.transactionId = transactionId;
        this.doitRepondreQuestion = doitRepondreQuestion;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }
    public int getId() {
        return id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getTitre() {
        return titre;
    }

    public boolean isLu() {
        return lu;
    }

    public String getDateRecu() {
        return dateRecu;
    }

    public String getContenu() {
        return contenu;
    }

    public int getCompteId() {
        return compteId;
    }



}
