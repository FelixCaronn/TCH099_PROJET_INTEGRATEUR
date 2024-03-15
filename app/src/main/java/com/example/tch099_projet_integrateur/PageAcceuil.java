package com.example.tch099_projet_integrateur;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.mariadb.jdbc.Driver;

import java.sql.*;

public class PageAcceuil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_acceuil);

        if(ConnexionBD.connexion())
        {
            Log.i("MesMessages", "Connexion etablie");
        }
        else
        {
            Log.i("MesMessages", "Erreur de connexion");

        }

    }
}