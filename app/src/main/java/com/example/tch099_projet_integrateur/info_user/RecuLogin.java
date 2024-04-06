package com.example.tch099_projet_integrateur.info_user;

/**
 * Classe représentant les données reçues après une tentative de connexion.
 */
public class RecuLogin {

    private int code;
    private String reponse;
    private String nom;
    private String courriel;
    private int id;

    /**
     * Constructeur par défaut de la classe RecuLogin.
     * Initialise les valeurs par défaut.
     */
    public RecuLogin() {
        code = 0;
        reponse = "";
        nom = "";
    }

    /**
     * Obtient l'identifiant de l'utilisateur.
     * @return L'identifiant de l'utilisateur.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'utilisateur.
     * @param id L'identifiant de l'utilisateur.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le nom de l'utilisateur.
     * @return Le nom de l'utilisateur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'utilisateur.
     * @param nom Le nom de l'utilisateur.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient le code de réponse de la connexion.
     * @return Le code de réponse de la connexion.
     */
    public int getCode() {
        return code;
    }

    /**
     * Obtient la réponse de la connexion.
     * @return La réponse de la connexion.
     */
    public String getReponse() {
        return reponse;
    }

    /**
     * Définit le code de réponse de la connexion.
     * @param code Le code de réponse de la connexion.
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Définit la réponse de la connexion.
     * @param reponse La réponse de la connexion.
     */
    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public void setCourriel(String courriel) { this.courriel = courriel;}
    public String getCourriel() { return this.courriel; }
}
