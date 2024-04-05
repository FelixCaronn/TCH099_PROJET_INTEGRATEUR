package com.example.tch099_projet_integrateur.info_user;

/**
 * Représente une notification pour un utilisateur.
 */
public class Notifications {
    private int id, compteId, transactionId, lu, enAttente;
    private String titre, dateRecu, contenu, question, reponse;
    private boolean doitRepondreQuestion;

    /**
     * Constructeur pour une notification avec question et réponse.
     *
     * @param id L'identifiant de la notification.
     * @param compteId L'identifiant du compte associé à la notification.
     * @param transactionId L'identifiant de la transaction associée à la notification.
     * @param titre Le titre de la notification.
     * @param contenu Le contenu de la notification.
     * @param dateRecu La date de réception de la notification.
     * @param lu Indique si la notification a été lue.
     * @param enAttente Indique si la notification est en attente.
     * @param question La question associée à la notification.
     * @param reponse La réponse associée à la notification.
     */
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

    /**
     * Constructeur pour une notification sans question ni réponse.
     *
     * @param id L'identifiant de la notification.
     * @param compteId L'identifiant du compte associé à la notification.
     * @param transactionId L'identifiant de la transaction associée à la notification.
     * @param titre Le titre de la notification.
     * @param contenu Le contenu de la notification.
     * @param dateRecu La date de réception de la notification.
     * @param lu Indique si la notification a été lue.
     * @param enAttente Indique si la notification est en attente.
     */
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


    /**
     * Définit l'état de lecture de la notification.
     *
     * @param lu L'état de lecture de la notification.
     */

    public void setLu(int lu) {
        this.lu = lu;
    }

    // Getters
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
