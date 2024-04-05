package com.example.tch099_projet_integrateur;

import com.example.tch099_projet_integrateur.enumerations.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.mariadb.jdbc.Driver;

import java.sql.*;

/**
 * Cette activité représente la page d'accueil de l'application.
 * Elle permet à l'utilisateur de choisir entre se connecter ou créer un nouveau compte.
 */
public class PageAcceuil extends AppCompatActivity {

    // Déclaration des boutons de la page d'accueil
    Button btnConnexion;
    Button btnCreer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_acceuil);

        // Initialisation des boutons et ajout des écouteurs d'événements
        btnConnexion = findViewById(R.id.btnConnexion);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'activité de connexion lorsque le bouton "Connexion" est cliqué
                PagePrincipale.redirectActivity(PageAcceuil.this, PageConnection.class);
            }
        });

        btnCreer = findViewById(R.id.btnCreer);
        btnCreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'activité de création de compte lorsque le bouton "Créer" est cliqué
                PagePrincipale.redirectActivity(PageAcceuil.this, CreerCompte.class);
            }
        });
    }
}
