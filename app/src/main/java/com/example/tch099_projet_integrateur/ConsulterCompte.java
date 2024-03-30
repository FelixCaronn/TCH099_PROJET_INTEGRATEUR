package com.example.tch099_projet_integrateur;
import com.example.tch099_projet_integrateur.enumerations.*;
import com.example.tch099_projet_integrateur.info_user.TransactionBancaire;
import com.example.tch099_projet_integrateur.info_user.CompteBancaire;
import com.example.tch099_projet_integrateur.info_user.historiqueAdapter;

import static com.example.tch099_projet_integrateur.PagePrincipale.openDrawer;
import static com.example.tch099_projet_integrateur.PagePrincipale.redirectActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    private CompteBancaire compte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_compte);
        Intent intent = this.getIntent();
        numeroCompte = intent.getIntExtra("NUM_COMPTE",0);
        solde = intent.getDoubleExtra("SOLDE_COMPTE",0);
        typeDuCompte = (typeCompte) intent.getSerializableExtra("TYPE_COMPTE");

        try{
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
        listeHisto = findViewById(R.id.listeHistorique);
        typeCompte = findViewById(R.id.typeCompte);
        typeCompte.setText(typeDuCompte.toString());
        numCompte = findViewById(R.id.txtnumeroCompte);
        numCompte.setText(Integer.toString(numeroCompte));
        soldeCompte = findViewById(R.id.txtMontantCompte);
        soldeCompte.setText(Double.toString(solde) + "$");
//        historique = new ArrayList<TransactionBancaire>();
        //test pour le scrollview, a remplacer par le vrai historique
//        TransactionBancaire transact;
//        for (int i = 0; i < 10; i++) {
//            transact = new TransactionBancaire(1,"2024/03/11",
//                    100,typeTransaction.PAIEMENTFACTURE,"allo");
//            historique.add(transact);
//        }
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