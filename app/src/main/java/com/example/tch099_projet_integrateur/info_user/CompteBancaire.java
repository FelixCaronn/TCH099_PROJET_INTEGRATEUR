package com.example.tch099_projet_integrateur.info_user;

import com.example.tch099_projet_integrateur.enumerations.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Représente un compte bancaire d'un utilisateur.
 */
public class CompteBancaire {
    private int num_compte;
    private double solde;
    private typeCompte typeCompte;

    private ArrayList<TransactionBancaire> listeTransactions;

    /**
     * Constructeur par défaut de la classe CompteBancaire.
     * Initialise la liste des transactions.
     */
    public CompteBancaire() {
        this.listeTransactions = new ArrayList<>();
    }

    /**
     * Constructeur de la classe CompteBancaire.
     *
     * @param _num_compte Le numéro du compte bancaire.
     * @param _solde Le solde du compte bancaire.
     * @param _typeCompte Le type de compte bancaire (ex: CHEQUE, EPARGNE, etc.).
     */
    public CompteBancaire(int _num_compte, double _solde, typeCompte _typeCompte) {
        this.num_compte = _num_compte;
        this.solde = _solde;
        this.typeCompte = _typeCompte;
    }

    /**
     * @return La liste des transactions associées à ce compte bancaire.
     */
    public ArrayList<TransactionBancaire> getListeTransactions() {
        return listeTransactions;
    }

    /**
     * Définit la liste des transactions associées à ce compte bancaire.
     *
     * @param listeTransactions La liste des transactions associées à ce compte bancaire.
     */
    public void setListeTransactions(ArrayList<TransactionBancaire> listeTransactions) {
        this.listeTransactions = listeTransactions;
    }

    /**
     * Ajoute une transaction à la liste des transactions de ce compte bancaire.
     *
     * @param transaction La transaction à ajouter.
     */
    public void addTransaction(TransactionBancaire transaction) {
        this.listeTransactions.add(transaction);
    }

    /**
     * Définit le numéro du compte bancaire.
     *
     * @param n Le numéro du compte bancaire.
     */
    public void setNumCompte(int n) {
        this.num_compte = n;
    }

    /**
     * @return Le numéro du compte bancaire.
     */
    public int getNumCompte() {
        return num_compte;
    }

    /**
     * Définit le solde du compte bancaire.
     *
     * @param s Le solde du compte bancaire.
     */
    public void setSolde(double s) {
        this.solde = s;
    }

    /**
     * @return Le solde du compte bancaire.
     */
    public double getSolde() {
        return solde;
    }

    /**
     * @return Le type de compte bancaire.
     */
    public typeCompte getTypeCompte() {
        return typeCompte;
    }
}
