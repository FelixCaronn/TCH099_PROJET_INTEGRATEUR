package com.example.tch099_projet_integrateur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.tch099_projet_integrateur.info_user.NotificationAdapter;
import com.example.tch099_projet_integrateur.info_user.Notifications;
import com.example.tch099_projet_integrateur.info_user.historiqueAdapter;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;
    ListView listViewNoti;
    List<Notifications> arrayNoti;
    Button toutEffacer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        listViewNoti = findViewById(R.id.listNoti);
        toutEffacer = findViewById(R.id.toutEffacer);
        arrayNoti = new ArrayList<Notifications>();


        //test creer des notiifcations aleatoires
        for (int i = 0; i < 5; i++) {
            arrayNoti.add(new Notifications(i,i,i,"titre" + Integer.toString(i),"contenu" + Integer.toString(i), "2024/04/01",false,(i%2==0)));
        }

        NotificationAdapter adapter = new NotificationAdapter(this,R.layout.notification_layout,arrayNoti);
        listViewNoti.setAdapter(adapter);

        //delete toute les notifications qui ne sont pas pour un virement pas encore accepte
        toutEffacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        //ÉLÉMENTS DU MENU
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
                redirectActivity(Notification.this, PagePrincipale.class);
            }
        });

        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Notification.this, DepotCheque.class);
            }
        });

        facture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Notification.this, paiementFacture.class);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Notification.this, SupportNautico.class);
            }
        });

        transfertClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Notification.this, virementEntreCompte.class);
            }
        });

    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}