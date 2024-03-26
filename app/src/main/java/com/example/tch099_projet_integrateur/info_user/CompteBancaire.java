package com.example.tch099_projet_integrateur.info_user;

import com.example.tch099_projet_integrateur.enumerations.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

public class CompteBancaire {
    private int num_compte;
    private double solde;
    private typeCompte typeCompte;

    public CompteBancaire() {

    }
    public CompteBancaire(int _num_compte, double _solde, typeCompte _typeCompte)
    {
        this.num_compte = _num_compte;
        this.solde = _solde;
        this.typeCompte = _typeCompte;
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
