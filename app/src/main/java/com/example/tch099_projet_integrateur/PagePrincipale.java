package com.example.tch099_projet_integrateur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.drawable.Drawable;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Cette activité représente la page principale de l'application où l'utilisateur peut consulter ses comptes bancaires.
 */
public class PagePrincipale extends AppCompatActivity {

    // Déclaration des éléments de l'interface utilisateur
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte, btnDeconnexion;
    TextView bjrTxt, soldeCourant;
    ListView lvComptes;
    Button btnNautico;

    // Utilisateur actuellement connecté
    public static Utilisateur user = new Utilisateur();

    // Liste des comptes bancaires de l'utilisateur
    private List<CompteBancaire> lesComptes;

    // Adaptateur pour afficher les comptes bancaires dans la ListView
    private CompteAdapter adaptateur;

    // Calendrier pour gérer la session de l'utilisateur
    static Calendar calendrier = Calendar.getInstance();
    static Date endTime;



    private  ArrayList<Float> solde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupération des comptes de l'utilisateur depuis la base de données
        try {
            ArrayList<CompteBancaire> liste = ConnexionBD.getComptes(user.getId());
            user.setListeComptes(liste);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Vérification de la session de l'utilisateur
        verifSession(this);

        // Configuration de l'interface utilisateur
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
        btnDeconnexion = findViewById(R.id.btnDeconnexion);
        lvComptes = findViewById(R.id.lvComptes);
        btnNautico = findViewById(R.id.btnNautico);
        soldeCourant = findViewById(R.id.txtSoldeCourrant);


        // Gestion des événements sur les éléments de l'interface utilisateur
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        // Redirection vers la page de dépôt de chèque
        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, DepotCheque.class);
                DepotCheque.user.setId(user.getId());
            }
        });

        // Redirection vers la page de transfert entre comptes
        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, virementEntreCompte.class);
            }
        });

        // Redirection vers la page de paiement de facture
        facture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, paiementFacture.class);
            }
        });

        // Redirection vers la page de notification
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, Notification.class);
            }
        });

        // Redirection vers la page de support
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, SupportNautico.class);
            }
        });

        // Redirection vers la page de transfert entre utilisateurs
        transfertClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(PagePrincipale.this, virementEntreUtilisateurs.class);
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

        // Affichage des comptes bancaires de l'utilisateur
        lesComptes = user.getListeComptes();
        adaptateur = new CompteAdapter(this, R.layout.layout_compte, lesComptes);
        lvComptes.setAdapter(adaptateur);

        // Gestion des événements de clic sur les comptes pour afficher leurs détails
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

        // Gestion du clic sur le bouton d'assistance
        btnNautico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Assistance = new Intent(getApplicationContext(), SupportNautico.class);
                startActivity(Assistance);
            }
        });

        //Call API afin d'obtenir les sommes des 7 derniers jours
        try {
            solde = ConnexionBD.getSommeComptes(user.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String soldecour = solde.get(solde.size()-1).toString();
        soldeCourant.setText(soldecour+"$");

        //Prendre graphique
        LineChart graph = (LineChart) findViewById(R.id.chart);

        List<Entry> donnees = new ArrayList<Entry>();
        for(int i = 0; i < solde.size(); i++)
        {
            //Ajout des donnees
            donnees.add(new Entry(i, solde.get(i)));
        }

        //Initialiser le dataSet
        LineDataSet data = new LineDataSet(donnees, "Solde");
        data.setDrawValues(false);
        data.setValueTextSize(0);
        data.setDrawCircles(false);


        data.setDrawFilled(true);
        //Initialisation d'un drawable afin d'afficher le gradient sous le courbe
        if(Utils.getSDKInt() >= 18)
        {
            Drawable drawable = ContextCompat.getDrawable(this,R.drawable.graphique_gradient);
            data.setFillDrawable(drawable);
        }

        LineData lineData = new LineData(data);
        lineData.setDrawValues(true);

        //Cacher les axes du graphiques
        graph.setData(lineData);
        graph.getAxisLeft().setDrawLabels(false);
        graph.getAxisRight().setDrawLabels(false);
        graph.getAxisRight().setDrawGridLines(false);
        graph.getAxisRight().setDrawAxisLine(false);
        graph.getAxisLeft().setDrawGridLines(false);
        graph.getAxisLeft().setDrawAxisLine(false);
        graph.getXAxis().setDrawGridLines(false);
        graph.getXAxis().setDrawLabels(false);
        graph.getXAxis().setDrawAxisLine(false);
        graph.setDescription(new Description());
        graph.setDrawGridBackground(false);
        graph.setDrawBorders(false);
        graph.setAutoScaleMinMaxEnabled(true);
        graph.setBorderWidth(0);
        graph.getDescription().setEnabled(false);

        // hide legend
        Legend legend = graph.getLegend();
        legend.setEnabled(false);


        graph.invalidate();

    }

    // Ouvrir le tiroir de navigation
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    // Fermer le tiroir de navigation
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // Rediriger vers une autre activité
    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    // Vérifier la session de l'utilisateur
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

    // Mettre à jour la liste des comptes bancaires lors de la reprise de l'activité
    @Override
    protected void onResume() {
        super.onResume();
        lesComptes = user.getListeComptes();
        adaptateur = new CompteAdapter(this, R.layout.layout_compte, lesComptes);
        lvComptes.setAdapter(adaptateur);
    }

    // Vérifier la session de l'utilisateur
    public static void verifSession(Activity activity) {
        // Récupérer la date actuelle
        Date dateCurrent = Calendar.getInstance().getTime();

        // Si la date est postérieure à la fin de la session, déconnecter l'utilisateur
        if (!dateCurrent.before(PagePrincipale.endTime)) {
            // Arrêter toutes les activités
            activity.finishAffinity();

            // Rediriger l'utilisateur vers la page de connexion
            Intent intent = new Intent(activity.getApplicationContext(), PageConnection.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);

            Toast.makeText(activity.getApplicationContext(), "Votre session a expiré!", Toast.LENGTH_SHORT).show();
        }
        // Sinon, actualiser la fin de la session
        else {
            PagePrincipale.calendrier.add(Calendar.SECOND, 300);
            PagePrincipale.endTime = PagePrincipale.calendrier.getTime();
        }
    }
}
