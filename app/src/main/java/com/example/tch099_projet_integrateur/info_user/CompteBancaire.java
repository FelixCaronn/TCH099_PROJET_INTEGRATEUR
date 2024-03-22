package com.example.tch099_projet_integrateur.info_user;

import com.example.tch099_projet_integrateur.enumerations.*;

import java.util.ArrayList;

public class CompteBancaire {
    private int num_compte;
    private double solde;
    private typeCompte typeCompte;
    private ArrayList transactions;

    public CompteBancaire() {

    }
    public CompteBancaire(int _num_compte, double _solde, typeCompte _typeCompte)
    {
        this.num_compte = _num_compte;
        this.solde = _solde;
        this.typeCompte = _typeCompte;

        transactions = new ArrayList<>();
    }

    public void setNumCompte(int n)
    {
        this.num_compte = n;
    }

    public int getNumCompte()
    {
        return num_compte;
    }

    public void setSolde(double s)
    {
        this.solde = s;
    }

    public double getSolde()
    {
        return solde;
    }

    public typeCompte getTypeCompte()
    {
        return typeCompte;
    }
}
