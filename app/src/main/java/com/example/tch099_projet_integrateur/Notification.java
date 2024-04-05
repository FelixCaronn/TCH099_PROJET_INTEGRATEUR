package com.example.tch099_projet_integrateur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tch099_projet_integrateur.info_user.NotificationAdapter;
import com.example.tch099_projet_integrateur.info_user.Notifications;
import com.example.tch099_projet_integrateur.info_user.RecuLogin;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette activité gère l'affichage et la gestion des notifications pour l'utilisateur.
 * Les notifications peuvent inclure des demandes de virement, des messages importants, etc.
 */
public class Notification extends AppCompatActivity {

    // Déclaration des variables membres
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;
    ListView listViewNoti;
    List<Notifications> arrayNoti;
    static NotificationAdapter adapter;
    Button toutEffacer;
    RecuLogin recuLogin;

    // Réponses des API et dialogues
    private static String response;
    private static ArrayList<String> responseAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Vérifier que la session n'est pas expirée
        PagePrincipale.verifSession(this);

        // Initialisation des éléments de l'interface utilisateur
        listViewNoti = findViewById(R.id.listNoti);
        toutEffacer = findViewById(R.id.toutEffacer);
        arrayNoti = new ArrayList<Notifications>();

        // Récupération des notifications depuis l'API backend
        try {
            arrayNoti = ConnexionBD.getNotifications(PagePrincipale.user.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Initialisation de l'adaptateur pour la liste des notifications
        adapter = new NotificationAdapter(this,R.layout.notification_layout,arrayNoti);
        listViewNoti.setAdapter(adapter);

        // Gestionnaire de clics sur les éléments de la liste des notifications
        listViewNoti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Gérer le clic sur une notification spécifique
                Notifications notifSelectionnee = (Notifications) parent.getAdapter().getItem(position);
                if(notifSelectionnee.getEnAttente() == 1) {
                    try {
                        // Afficher un dialogue pour gérer la réponse à la notification
                        showDialog(notifSelectionnee.getQuestion(), notifSelectionnee.getTransactionId(),
                                notifSelectionnee.getCompteId());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(Notification.this,"Ceci n'est pas un virement", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Gestionnaire de clics sur le bouton "Effacer tout"
        toutEffacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Supprimer toutes les notifications
                try {
                    ConnexionBD.deleteNotif(PagePrincipale.user.getId(), -1, "/deleteAll");
                    // Mettre à jour la liste des notifications après la suppression
                    arrayNoti.clear();
                    arrayNoti.addAll(ConnexionBD.getNotifications(PagePrincipale.user.getId()));
                    adapter.notifyDataSetChanged();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Gestion des éléments du menu latéral
        drawerLayout = findViewById(R.id.drawerLayout);
        // ...
    }

    // Méthode pour afficher le menu latéral
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    // Méthode pour fermer le menu latéral
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // Méthode pour rediriger vers une autre activité
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

    // Méthode pour afficher le dialogue de réponse à la notification
    private void showDialog(String question, int idTransaction, int idUser) throws InterruptedException {
        // ...
    }
}
