package com.example.tch099_projet_integrateur.info_user;

public class DaoSingleton {
    private static ComptesDao daoInstance = null;
    public static ComptesDao getDaoInstance()
    {
        if (daoInstance == null)
        {
            daoInstance = new ComptesDao();
        }

        return daoInstance;
    }
}
