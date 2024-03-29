package com.example.tch099_projet_integrateur.info_user;
import com.example.tch099_projet_integrateur.enumerations.*;

public class TransactionBancaire {

    //Provenance du virement
    private int compteProvenance;

    //ID du compte recevant
    private int idCompteRecevant;

    //Date de la transaction
    private String dateDeTransaction;

    //Destinataire payment de facture
    private String nomEtablissement;

    //Montant de la transaction
    private double montant;

    //Type de transaction
    private typeTransaction type;

    //Provenance d'un transfert
    private String provenance;

    //Facture
    public TransactionBancaire(typeTransaction _type, String _nomEtablissment, String date, double _montant) {

        type = _type;
        nomEtablissement = _nomEtablissment;
        dateDeTransaction = date;
        montant = _montant;

    }

    //Depot
    public TransactionBancaire(typeTransaction _type, String _date, double _montantDepot ){

        type = _type;
        dateDeTransaction = _date;
        montant = _montantDepot;

    }

    //Transfert entre comptes
    public TransactionBancaire(typeTransaction _type, int idProvenance, String dateTransfert, double _montantTransfert){

        type = _type;
        idCompteRecevant = idProvenance;
        dateDeTransaction = dateTransfert;
        montant = _montantTransfert;

    }

    //Virement entre personnes
    public TransactionBancaire(typeTransaction _type, String etablissement){

        type = _type;
        nomEtablissement = etablissement;


    }


    public String getProvenance() {
        return provenance;
    }

    public int getCompteProvenance() {
        return compteProvenance;
    }

    public String getDateDeTransaction() {
        return dateDeTransaction;
    }
    public double getMontant() {
        return montant;
    }
    public typeTransaction getType() {
        return type;
    }

    public int getIdCompteRecevant() {
        return idCompteRecevant;
    }

    public String getNomEtablissement() {
        return nomEtablissement;
    }

    public void setCompteProvenance(int compteProvenance) {
        this.compteProvenance = compteProvenance;
    }

    public void setDateDeTransaction(String date) {
        this.dateDeTransaction = date;
    }

    public void setIdCompteRecevant(int idCompteRecevant) {
        this.idCompteRecevant = idCompteRecevant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

}
