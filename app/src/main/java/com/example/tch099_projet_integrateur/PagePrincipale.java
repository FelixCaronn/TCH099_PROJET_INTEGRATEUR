package com.example.tch099_projet_integrateur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tch099_projet_integrateur.enumerations.typeCompte;
import com.example.tch099_projet_integrateur.info_user.CompteAdapter;
import com.example.tch099_projet_integrateur.info_user.CompteBancaire;
import com.example.tch099_projet_integrateur.info_user.Utilisateur;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte, btnDeconnexion;

    static Calendar calendrier = Calendar.getInstance();
    static Date endTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ArrayList<CompteBancaire> liste = ConnexionBD.getComptes(user.getId());
            user.setListeComptes(liste);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Ajuster la date de fin de la session
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        calendrier.add(Calendar.SECOND, 3);
        endTime = calendrier.getTime();


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

        //Boutons de la barre de menu
        home = findViewById(R.id.home);
        depot = findViewById(R.id.depot);
        facture = findViewById(R.id.facture);
        notification = findViewById(R.id.notification);
        support = findViewById(R.id.support);
        transfertClient = findViewById(R.id.transfertClient);
        transfertCompte = findViewById(R.id.transfertCompte);
        btnDeconnexion = findViewById(R.id.btnDeconnexion);





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
                DepotCheque.user.setId(user.getId());
            }
        });

        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, virementEntreCompte.class);
            }
        });

        facture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, paiementFacture.class);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, Notification.class);
            }
        });

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

        btnDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, PageConnection.class);
            }
        });


        lvComptes = findViewById(R.id.lvComptes);
        btnNautico = findViewById(R.id.btnNautico);

        //Afficher les comptes
        lesComptes = user.getListeComptes();
        adaptateur = new CompteAdapter(this, R.layout.layout_compte, lesComptes);
        lvComptes.setAdapter(adaptateur);

        //Ajouter écouteur d'événement sur chaque compte pour afficher leurs détails
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

    //Fonction pour vérifier la session à chaque page
    public static void verifSession(Activity activity) {
        //Chercher le temps présentement
        Date dateCurrent = Calendar.getInstance().getTime();

        //Si la date est avant la dernière activité, on déconnecte la personne
        if (!dateCurrent.before(PagePrincipale.endTime)) {
            //On finit toutes les activités
            activity.finishAffinity();

            //On redirige l'utilisateur vers la 1ère activité
            Intent intent = new Intent(activity.getApplicationContext(), PageConnection.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);

            Toast.makeText(activity.getApplicationContext(), "Votre session a expiré!", Toast.LENGTH_SHORT).show();
        }

        //Sinon, on actualise le end time
        else {
            PagePrincipale.calendrier.add(Calendar.SECOND, 3);
            PagePrincipale.endTime = PagePrincipale.calendrier.getTime();
        }
    }
}