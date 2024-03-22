package com.example.tch099_projet_integrateur.info_user;
import com.example.tch099_projet_integrateur.enumerations.*;

public class TransactionBancaire {
    private String compteRecevant;
    private String dateDeTransaction;
    private double montant;
    private typeTransaction type;
    private String provenance;

    public TransactionBancaire(String compteRecevant, String dateDeTransaction, double montant,typeTransaction type,String provenance) {
        this.compteRecevant = compteRecevant;
        this.dateDeTransaction = dateDeTransaction;
        this.montant = montant;
        this.type = type;
        this.provenance = provenance;
    }

    public String getProvenance() {
        return provenance;
    }
    public String getCompteRecevant() {
        return compteRecevant;
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
}
