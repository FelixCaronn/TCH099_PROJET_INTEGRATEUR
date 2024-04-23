package com.example.tch099_projet_integrateur.info_user;

import com.example.tch099_projet_integrateur.enumerations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un utilisateur.
 */
public class Utilisateur {

    private String nom;
    private int id;
    private String courriel;

    private ArrayList<CompteBancaire> listeComptes;

    /**
     * Constructeur par défaut de la classe Utilisateur.
     * Initialise la liste des comptes bancaires.
     */
    public Utilisateur(){
        listeComptes = new ArrayList<CompteBancaire>();
    }

    /**
     * Constructeur de la classe Utilisateur avec une liste de comptes bancaires donnée.
     * @param list La liste des comptes bancaires de l'utilisateur.
     */
    public Utilisateur(ArrayList<CompteBancaire> list){
        listeComptes = list;
    }

    /**
     * Définit l'identifiant de l'utilisateur.
     * @param id L'identifiant de l'utilisateur.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient l'identifiant de l'utilisateur.
     * @return L'identifiant de l'utilisateur.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit la liste des comptes bancaires de l'utilisateur.
     * @param listeComptes La liste des comptes bancaires de l'utilisateur.
     */
    public void setListeComptes(ArrayList<CompteBancaire> listeComptes) {
        this.listeComptes = listeComptes;
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
     * Obtient la liste des comptes bancaires de l'utilisateur.
     * @return La liste des comptes bancaires de l'utilisateur.
     */
    public ArrayList<CompteBancaire> getListeComptes() {
        return listeComptes;
    }

    /**
     * Ajoute un compte bancaire à la liste des comptes de l'utilisateur.
     * @param cpt Le compte bancaire à ajouter.
     */
    public void addCompte(CompteBancaire cpt)
    {
        this.listeComptes.add(cpt);
    }

    public void setCourriel(String courriel) { this.courriel = courriel; }
    public String getCourriel() { return this.courriel; }

    public double getSoldeTotal()
    {
        double solde = 0;

        for(CompteBancaire cmpt: this.listeComptes)
        {
            solde += cmpt.getSolde();
        }

        return solde;
    }
}
