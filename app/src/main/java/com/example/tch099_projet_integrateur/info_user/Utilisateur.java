package com.example.tch099_projet_integrateur.info_user;
import com.example.tch099_projet_integrateur.enumerations.*;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

    private String nom;
    private int id;

    private ArrayList<CompteBancaire> listeComptes;

    public Utilisateur(){

        listeComptes = new ArrayList<CompteBancaire>();
    }

    public Utilisateur(ArrayList<CompteBancaire> list){
        listeComptes = list;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setListeComptes(ArrayList<CompteBancaire> listeComptes) {
        this.listeComptes = listeComptes;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<CompteBancaire> getListeComptes() {
        return listeComptes;
    }

    public void addCompte(CompteBancaire cpt)
    {
        this.listeComptes.add(cpt);
    }
}
