package com.example.tch099_projet_integrateur;
import com.example.tch099_projet_integrateur.enumerations.*;
import com.example.tch099_projet_integrateur.info_user.CompteBancaire;
import com.example.tch099_projet_integrateur.info_user.ComptesDao;
import com.example.tch099_projet_integrateur.info_user.DaoSingleton;

import static com.example.tch099_projet_integrateur.PagePrincipale.openDrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConsulterCompte extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;


    private TextView txtNum, txtSolde, txtType;
    private Button btnRetour;
    private CompteBancaire compte;

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

        txtNum = findViewById(R.id.txtnumeroCompte);
        txtSolde = findViewById(R.id.txtMontantCompte);
        txtType = findViewById(R.id.typeCompte);

        btnRetour = findViewById(R.id.btnRetour);

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        Intent intent = getIntent();

        typeCompte typeCompte = (typeCompte) intent.getSerializableExtra("TYPE_COMPTE");
        int numCompte = intent.getIntExtra("NUM_COMPTE", 0);
        double soldeCompte = intent.getDoubleExtra("SOLDE_COMPTE", 0.0);

        ComptesDao dao = DaoSingleton.getDaoInstance();
        compte = dao.getCompteParNum(String.valueOf(numCompte));
        txtType.setText(String.valueOf(typeCompte));
        txtNum.setText(String.valueOf(numCompte));
        txtSolde.setText(String.valueOf(soldeCompte));
    }
}