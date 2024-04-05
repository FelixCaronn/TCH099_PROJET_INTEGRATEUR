package com.example.tch099_projet_integrateur.info_user;

public class Notifications {
    private int id, compteId, transactionId, lu, enAttente;
    private String titre, dateRecu, contenu, question, reponse;
    private boolean doitRepondreQuestion;
    public Notifications(int id, int compteId, int transactionId, String titre, String contenu, String dateRecu, int lu, int enAttente, String question, String reponse){
        this.lu = lu;
        this.compteId = compteId;
        this.contenu = contenu;
        this.dateRecu = dateRecu;
        this.titre = titre;
        this.id = id;
        this.transactionId = transactionId;
        this.enAttente = enAttente;
        this.question = question;
        this.reponse = reponse;



        //this.doitRepondreQuestion = doitRepondreQuestion;
    }

    //Constructeur pour AUTRE notification
    public Notifications(int id, int compteId, int transactionId, String titre, String contenu, String dateRecu, int lu, int enAttente){
        this.lu = lu;
        this.compteId = compteId;
        this.contenu = contenu;
        this.dateRecu = dateRecu;
        this.titre = titre;
        this.id = id;
        this.transactionId = transactionId;
        this.enAttente = enAttente;

        //this.doitRepondreQuestion = doitRepondreQuestion;
    }

    public String getReponse() {
        return reponse;
    }

    public String getQuestion() {
        return question;
    }

    public void setLu(int lu) {
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

    public int isLu() {
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

    public int getEnAttente() {
        return enAttente;
    }
}
