package com.example.tch099_projet_integrateur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Cette activité permet à l'utilisateur d'envoyer un message de support.
 */
public class SupportNautico extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte, btnDeconnexion;
    EditText editTextTitreSupport;
    EditText editTextTextMultiLine;
    Button btnEnvoyerSupport;
    Button btnAnnuler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_nautico);

        // Récupération des éléments de l'interface utilisateur
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        depot = findViewById(R.id.depot);
        facture = findViewById(R.id.facture);
        notification = findViewById(R.id.notification);
        support = findViewById(R.id.support);
        transfertClient = findViewById(R.id.transfertClient);
        transfertCompte = findViewById(R.id.transfertCompte);
        btnDeconnexion = findViewById(R.id.btnDeconnexion);

        editTextTitreSupport = findViewById(R.id.editTextTitreSupport);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        btnEnvoyerSupport = findViewById(R.id.btnEnvoyerSupport);
        btnAnnuler = findViewById(R.id.btnAnnuler);

        // Gestion des clics sur les éléments de l'interface utilisateur
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.openDrawer(drawerLayout);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(SupportNautico.this, PagePrincipale.class);
            }
        });

        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(SupportNautico.this, DepotCheque.class);
            }
        });

        facture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(SupportNautico.this, paiementFacture.class);
            }
        });

        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(SupportNautico.this, virementEntreCompte.class);
            }
        });

        transfertClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(SupportNautico.this, virementEntreUtilisateurs.class);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(SupportNautico.this, Notification.class);
            }
        });

        // Déconnexion de l'utilisateur
        btnDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arrêt de toutes les activités de l'application
                finishAffinity();

                // Redirection vers la page de connexion
                Intent intent = new Intent(getApplicationContext(), PageConnection.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Vous avez bien été déconnecté(e)", Toast.LENGTH_SHORT).show();
            }
        });

        // Gestion de l'envoi du message de support
        btnEnvoyerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titre = editTextTitreSupport.getText().toString();
                String message = editTextTextMultiLine.getText().toString();

                // Vérification des champs
                if (!titre.isEmpty() && !message.isEmpty()) {
                    editTextTitreSupport.setText("");
                    editTextTextMultiLine.setText("");
                    Toast.makeText(SupportNautico.this, "Message envoyé avec succès", Toast.LENGTH_SHORT).show();
                } else if (titre.isEmpty()) {
                    Toast.makeText(SupportNautico.this, "Veuillez saisir un titre", Toast.LENGTH_SHORT).show();
                } else if (message.isEmpty()) {
                    Toast.makeText(SupportNautico.this, "Veuillez saisir un message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Gestion de l'annulation
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
