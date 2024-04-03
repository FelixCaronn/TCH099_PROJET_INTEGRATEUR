package com.example.tch099_projet_integrateur;
import com.example.tch099_projet_integrateur.enumerations.*;

import static com.example.tch099_projet_integrateur.PagePrincipale.openDrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SupportNautico extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;
    EditText editTextTitreSupport;
    EditText editTextTextMultiLine;
    Button btnEnvoyerSupport;
    Button btnAnnuler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_nautico);

        drawerLayout = findViewById(R.id.drawerLayout);

        menu = findViewById(R.id.menu);

        home = findViewById(R.id.home);
        depot = findViewById(R.id.depot);
        facture = findViewById(R.id.facture);
        notification = findViewById(R.id.notification);
        support = findViewById(R.id.support);
        transfertClient = findViewById(R.id.transfertClient);
        transfertCompte = findViewById(R.id.transfertCompte);

        editTextTitreSupport = findViewById(R.id.editTextTitreSupport);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);

        btnEnvoyerSupport = findViewById(R.id.btnEnvoyerSupport);
        btnAnnuler = findViewById(R.id.btnAnnuler);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(SupportNautico.this, DepotCheque.class);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(SupportNautico.this, Notification.class);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        btnEnvoyerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String titre = editTextTitreSupport.getText().toString();
                String message = editTextTextMultiLine.getText().toString();

                if (!titre.isEmpty() && !message.isEmpty())
                {
                    editTextTitreSupport.setText("");
                    editTextTextMultiLine.setText("");
                    Toast.makeText(SupportNautico.this, "message envoyé avec succes", Toast.LENGTH_SHORT).show();
                }
                else if (titre.isEmpty())
                {
                    Toast.makeText(SupportNautico.this, "Veuillez saisir un titre", Toast.LENGTH_SHORT).show();
                }
                else if (message.isEmpty())
                {
                    Toast.makeText(SupportNautico.this, "Veuillez saisir un message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
    }
}