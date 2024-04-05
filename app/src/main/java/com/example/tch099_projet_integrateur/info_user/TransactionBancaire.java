package com.example.tch099_projet_integrateur.info_user;

import com.example.tch099_projet_integrateur.enumerations.*;

/**
 * Classe représentant une transaction bancaire.
 */
public class TransactionBancaire {

    //Provenance du virement
    private int compteProvenance;

    //ID du compte recevant
    private int idCompteRecevant;

    //Date de la transaction
    private String dateDeTransaction;

    //Destinataire du paiement de facture
    private String nomEtablissement;

    //Montant de la transaction
    private double montant;

    //Type de transaction
    private typeTransaction type;

    //Provenance d'un transfert
    private String provenance;

    //Constructeur pour une transaction de paiement de facture
    public TransactionBancaire(typeTransaction _type, String _nomEtablissment, String date, double _montant) {

        type = _type;
        nomEtablissement = _nomEtablissment;
        dateDeTransaction = date;
        montant = _montant;

    }

    //Constructeur pour un dépôt
    public TransactionBancaire(typeTransaction _type, String _date, double _montantDepot ){

        type = _type;
        dateDeTransaction = _date;
        montant = _montantDepot;

    }

    //Constructeur pour un transfert entre comptes
    public TransactionBancaire(typeTransaction _type, int idProvenance, String dateTransfert, double _montantTransfert){

        type = _type;
        idCompteRecevant = idProvenance;
        dateDeTransaction = dateTransfert;
        montant = _montantTransfert;

    }

    //Constructeur pour un virement entre personnes
    public TransactionBancaire(typeTransaction _type, String etablissement){

        type = _type;
        nomEtablissement = etablissement;

    }

    /**
     * Obtient la provenance d'un transfert.
     * @return La provenance du transfert.
     */
    public String getProvenance() {
        return provenance;
    }

    /**
     * Obtient le compte de provenance.
     * @return Le compte de provenance.
     */
    public int getCompteProvenance() {
        return compteProvenance;
    }

    /**
     * Obtient la date de la transaction.
     * @return La date de la transaction.
     */
    public String getDateDeTransaction() {
        return dateDeTransaction;
    }

    /**
     * Obtient le montant de la transaction.
     * @return Le montant de la transaction.
     */
    public double getMontant() {
        return montant;
    }

    /**
     * Obtient le type de transaction.
     * @return Le type de transaction.
     */
    public typeTransaction getType() {
        return type;
    }

    /**
     * Obtient l'identifiant du compte recevant.
     * @return L'identifiant du compte recevant.
     */
    public int getIdCompteRecevant() {
        return idCompteRecevant;
    }

    /**
     * Obtient le nom de l'établissement pour une transaction de paiement de facture.
     * @return Le nom de l'établissement.
     */
    public String getNomEtablissement() {
        return nomEtablissement;
    }

    /**
     * Définit le compte de provenance.
     * @param compteProvenance Le compte de provenance.
     */
    public void setCompteProvenance(int compteProvenance) {
        this.compteProvenance = compteProvenance;
    }

    /**
     * Définit la date de la transaction.
     * @param date La date de la transaction.
     */
    public void setDateDeTransaction(String date) {
        this.dateDeTransaction = date;
    }

    /**
     * Définit l'identifiant du compte recevant.
     * @param idCompteRecevant L'identifiant du compte recevant.
     */
    public void setIdCompteRecevant(int idCompteRecevant) {
        this.idCompteRecevant = idCompteRecevant;
    }

    /**
     * Définit le montant de la transaction.
     * @param montant Le montant de la transaction.
     */
    public void setMontant(double montant) {
        this.montant = montant;
    }

    /**
     * Définit le nom de l'établissement pour une transaction de paiement de facture.
     * @param nomEtablissement Le nom de l'établissement.
     */
    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    /**
     * Définit la provenance d'un transfert.
     * @param provenance La provenance du transfert.
     */
    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }
}
