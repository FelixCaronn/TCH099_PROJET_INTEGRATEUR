package com.example.tch099_projet_integrateur;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.tch099_projet_integrateur.enumerations.typeCompte;
import com.example.tch099_projet_integrateur.info_user.CompteAdapter;
import com.example.tch099_projet_integrateur.info_user.CompteBancaire;
import com.example.tch099_projet_integrateur.info_user.ComptesDao;
import com.example.tch099_projet_integrateur.info_user.DaoSingleton;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class PagePrincipale extends AppCompatActivity {

    private List<CompteBancaire> lesComptes;
    private ComptesDao dao;
    private ListView lvComptes;
    private Button btnNautico;
    private CompteAdapter adaptateur;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_page_principale);

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
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, DepotCheque.class);
            }
        });

//        facture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                redirectActivity(PagePrincipale.this, );
//            }
//        });

//        notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, SupportNautico.class);
            }
        });

//        transfertClient.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//        transfertCompte.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        lvComptes = findViewById(R.id.lvComptes);
        btnNautico = findViewById(R.id.btnNautico);

        lvComptes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent iDetailCompte = new Intent(getApplicationContext(), ConsulterCompte.class);
                CompteBancaire compteSelectionnee = (CompteBancaire) parent.getAdapter().getItem(position);
                typeCompte typeCompte = compteSelectionnee.getTypeCompte();
                int num = compteSelectionnee.getNumCompte();
                double solde = compteSelectionnee.getSolde();
                iDetailCompte.putExtra("TYPE_COMPTE", typeCompte);
                iDetailCompte.putExtra("NUM_COMPTE", num);
                iDetailCompte.putExtra("SOLDE_COMPTE", solde);
                startActivity(iDetailCompte);
            }
        });

        btnNautico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Assistance = new Intent(getApplicationContext(), SupportNautico.class);
                startActivity(Assistance);
            }
        });
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void onClick(View v) {
        if (v == btnNautico) {
            Intent intention = new Intent(this, SupportNautico.class);
            startActivity(intention);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dao = DaoSingleton.getDaoInstance();
        lesComptes = dao.getComptes();
        adaptateur = new CompteAdapter(this, R.layout.layout_compte, lesComptes);
        lvComptes.setAdapter(adaptateur);
    }
}