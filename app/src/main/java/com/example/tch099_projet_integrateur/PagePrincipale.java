package com.example.tch099_projet_integrateur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tch099_projet_integrateur.enumerations.typeCompte;
import com.example.tch099_projet_integrateur.info_user.CompteAdapter;
import com.example.tch099_projet_integrateur.info_user.CompteBancaire;
import com.example.tch099_projet_integrateur.info_user.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class PagePrincipale extends AppCompatActivity {

    public static Utilisateur user = new Utilisateur();
    private List<CompteBancaire> lesComptes;
    private TextView bjrTxt;
    private ListView lvComptes;
    private Button btnNautico;
    private CompteAdapter adaptateur;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        try {
            ArrayList<CompteBancaire> liste = ConnexionBD.getComptes(user.getId());
            user.setListeComptes(liste);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        Intent intention = getIntent();
//        user.setNom(intention.getStringExtra("nom"));
//        user.setId(intention.getIntExtra("id", -1));
//
//        bjrTxt = findViewById(R.id.bonjourUser);
//
//        String bjr = "Bonjour " + user.getNom();
//        bjrTxt.setText(bjr);

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

        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, virementEntreCompte.class);
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

        transfertClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, virementEntreUtilisateurs.class);
            }
        });

              transfertCompte.setOnClickListener(new View.OnClickListener() {
                @Override
                  public void onClick(View v) {

                    redirectActivity(PagePrincipale.this, virementEntreCompte.class);

                 }
              });


        lvComptes = findViewById(R.id.lvComptes);
        btnNautico = findViewById(R.id.btnNautico);

        lesComptes = user.getListeComptes();
        adaptateur = new CompteAdapter(this, R.layout.layout_compte, lesComptes);
        lvComptes.setAdapter(adaptateur);

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
        lesComptes = user.getListeComptes();
        adaptateur = new CompteAdapter(this, R.layout.layout_compte, lesComptes);
        lvComptes.setAdapter(adaptateur);
    }
}