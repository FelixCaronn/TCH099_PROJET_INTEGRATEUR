package com.example.tch099_projet_integrateur.info_user;

import com.example.tch099_projet_integrateur.enumerations.typeCompte;

import java.util.ArrayList;
import java.util.List;

public class ComptesDao {
    private List<CompteBancaire> comptes = new ArrayList<>();

    public ComptesDao()
    {
        CompteBancaire c;
        int[] num_compte = {1234, 5678, 9012, 0123};
        int[] soldes = {100, 2000, 1225, 550};
        typeCompte[] typeComptes = {typeCompte.CHEQUE, typeCompte.EPARGNE, typeCompte.CARTE_CREDIT, typeCompte.INVESTISSEMENT};

        for (int i = 0; i < num_compte.length; i++)
        {
            c = new CompteBancaire(num_compte[i], soldes[i], typeComptes[i]);
            comptes.add(c);
        }
    }

    public List<CompteBancaire> getComptes() { return comptes; }

    public CompteBancaire getCompteParNum(String num)
    {
        for (CompteBancaire c:comptes)
        {
            if (num.equals(c.getNumCompte()))
                return c;
        }
        return null;
    }
}
