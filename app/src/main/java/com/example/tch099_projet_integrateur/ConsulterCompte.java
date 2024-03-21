package com.example.tch099_projet_integrateur;
import com.example.tch099_projet_integrateur.enumerations.*;

import static com.example.tch099_projet_integrateur.PagePrincipale.openDrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ConsulterCompte extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;
    TextView typeCompte, numCompte;
    Button btnTransfert, btnVirement, btnFacture, btnCheque;
    ListView listeHisto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_compte);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        depot = findViewById(R.id.depot);
        facture = findViewById(R.id.facture);
        notification = findViewById(R.id.notification);
        support = findViewById(R.id.support);
        transfertClient = findViewById(R.id.transfertClient);
        transfertCompte = findViewById(R.id.transfertCompte);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        btnCheque = findViewById(R.id.btnDeposerCheque);
        btnFacture = findViewById(R.id.btnPayerFacture);
        btnTransfert = findViewById(R.id.btnTransfert);
        btnVirement = findViewById(R.id.btnEnvoyer);
        listeHisto = findViewById(R.id.listeHistorique);
        typeCompte = findViewById(R.id.typeCompte);
        typeCompte.setText("");
        numCompte = findViewById(R.id.numCompte);
        numCompte.setText("");






    }
}