package com.example.tch099_projet_integrateur;
import com.example.tch099_projet_integrateur.enumerations.*;
import com.example.tch099_projet_integrateur.info_user.TransactionBancaire;
import com.example.tch099_projet_integrateur.info_user.CompteBancaire;
import com.example.tch099_projet_integrateur.info_user.historiqueAdapter;
import com.google.android.material.timepicker.TimeFormat;

import static com.example.tch099_projet_integrateur.PagePrincipale.openDrawer;
import static com.example.tch099_projet_integrateur.PagePrincipale.redirectActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Activité permettant de consulter un compte bancaire.
 */
public class ConsulterCompte extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;
    TextView typeCompte, numCompte, soldeCompte;
    Button btnTransfert, btnVirement, btnFacture, btnCheque;
    ListView listeHisto;
    int numeroCompte;
    double solde;
    typeCompte typeDuCompte;
    List<TransactionBancaire> historique;

    private TextView txtNum, txtSolde, txtType;
    private Button btnRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_compte);

        PagePrincipale.verifSession(this);

        //Vérifier que la session n'est pas expirée
        PagePrincipale.verifSession(this);
        
        //Chercher les infos du compte
        Intent intent = this.getIntent();
        numeroCompte = intent.getIntExtra("NUM_COMPTE",0);
        //solde = intent.getDoubleExtra("SOLDE_COMPTE",0);
        typeDuCompte = (typeCompte) intent.getSerializableExtra("TYPE_COMPTE");

        try{
            solde = ConnexionBD.getCompte(numeroCompte);
            historique = ConnexionBD.getTransaction(numeroCompte);
        }catch (Exception e)
        {
            Log.e("TAG", "MARCHE PAS");
            e.printStackTrace();
        }

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

        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ConsulterCompte.this, DepotCheque.class);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ConsulterCompte.this, SupportNautico.class);
            }
        });

        transfertClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redirectActivity(ConsulterCompte.this, virementEntreUtilisateurs.class);

            }
        });

        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redirectActivity(ConsulterCompte.this, virementEntreCompte.class);

            }
        });

        btnCheque = findViewById(R.id.btnDeposerCheque);
        btnCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ConsulterCompte.this, DepotCheque.class);
            }
        });

        btnFacture = findViewById(R.id.btnPayerFacture);
        btnFacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ConsulterCompte.this, paiementFacture.class);
            }
        });

        btnTransfert = findViewById(R.id.btnTransfert);
        btnTransfert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ConsulterCompte.this, virementEntreCompte.class);
            }
        });

        btnVirement = findViewById(R.id.btnEnvoyer);
        btnVirement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ConsulterCompte.this, virementEntreUtilisateurs.class);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ConsulterCompte.this, Notification.class);
            }
        });

        //Afficher les informations du compte (type, numéro et solde)
        typeCompte = findViewById(R.id.typeCompte);
        typeCompte.setText(typeDuCompte.toString());
        numCompte = findViewById(R.id.txtnumeroCompte);
        numCompte.setText(Integer.toString(numeroCompte));
        soldeCompte = findViewById(R.id.txtMontantCompte);
        soldeCompte.setText(String.format("%.2f", solde) + "$");

        //Afficher l'historique des transactions dans l'adaptateur
        listeHisto = findViewById(R.id.listeHistorique);
        historiqueAdapter adapter = new historiqueAdapter(this,R.layout.historique_layout,historique);
        listeHisto.setAdapter(adapter);
        ////


        btnRetour = findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}
