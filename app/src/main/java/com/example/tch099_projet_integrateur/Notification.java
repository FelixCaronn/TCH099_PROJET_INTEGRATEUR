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

public class Notification extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;
    ListView listViewNoti;
    List<Notifications> arrayNoti;
    static NotificationAdapter adapter;
    Button toutEffacer;
    RecuLogin recuLogin;

    private static String response;
    
    private static  ArrayList<String> responseAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //Contenus de la page
        listViewNoti = findViewById(R.id.listNoti);
        toutEffacer = findViewById(R.id.toutEffacer);
        arrayNoti = new ArrayList<Notifications>();

        //Chercher les notifications avec l'API
        try {
            arrayNoti = ConnexionBD.getNotifications(PagePrincipale.user.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



        //test creer des notiifcations aleatoires
        //for (int i = 0; i < 5; i++) {
            //arrayNoti.add(new Notifications(i,i,i,"titre" + Integer.toString(i),"contenu" + Integer.toString(i), "2024/04/01",false,(i%2==0)));
        //}

        adapter = new NotificationAdapter(this,R.layout.notification_layout,arrayNoti);
        listViewNoti.setAdapter(adapter);




        listViewNoti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Prendre notification concernée
                Notifications notifSelectionnee = (Notifications) parent.getAdapter().getItem(position);
                if(notifSelectionnee.getEnAttente() == 1)
                {

                    try {
                        showDialog(notifSelectionnee.getQuestion(), notifSelectionnee.getTransactionId(),
                                notifSelectionnee.getCompteId());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                }
                else
                {
                    Toast.makeText(Notification.this,"Ceci n'est pas un virement", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //delete toute les notifications qui ne sont pas pour un virement pas encore accepte
        toutEffacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Faire une requête pour effacer toutes les notifs avec la route "deleteAll"
                try {
                    ConnexionBD.deleteNotif(PagePrincipale.user.getId(), -1, "/deleteAll");

                    arrayNoti.clear(); // Clear existing data
                    arrayNoti.addAll(ConnexionBD.getNotifications(PagePrincipale.user.getId())); // Update with new data
                    adapter.notifyDataSetChanged();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
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
                redirectActivity(Notification.this, virementEntreUtilisateurs.class);
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

    //Pop-up notification transfert
    private void showDialog(String question, int idTransaction, int idUser) throws InterruptedException {

        boolean clicked = false;

        Dialog dialog = new Dialog(this, R.style.popUpStyle);
        dialog.setContentView(R.layout.layout_notif_virement);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_background_transparent);

        TextView txtViewQuestion = dialog.findViewById(R.id.userQuestion);

        txtViewQuestion.setText(question);

        EditText txtReponse = dialog.findViewById(R.id.editTextUserReponse);
        Button btnAccepter = dialog.findViewById(R.id.btnAccepterVirement);
        Button btnRefuser = dialog.findViewById(R.id.btnRefuserVirement);
        ImageView quitter = dialog.findViewById(R.id.btnQuitter);

        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //Call API reception accepter virement
        btnAccepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtReponse.getText().toString().isEmpty())
                {
                    Toast.makeText(dialog.getContext(),"Champ de reponse vide", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    response = txtReponse.getText().toString();
                    //Call au API
                    try {
                        responseAPI = ConnexionBD.receptionTransfertEntreUtilisateur("accepter", response,
                                                                                    idTransaction, idUser);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    //transformer arraylist en String
                    String afficherText = null;
                    for(int i = 0; i < responseAPI.size(); i++)
                    {
                        afficherText = responseAPI.get(i) + "\n";
                    }

                    if(afficherText != null)
                    {
                        Toast.makeText(Notification.this, afficherText + "", Toast.LENGTH_LONG).show();
                    }

                    redirectActivity(Notification.this, Notification.class);
                }


            }
        });

        //Call API reception virement refuser
        btnRefuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                response = txtReponse.getText().toString();
                //Call au API
                try {
                    responseAPI = ConnexionBD.receptionTransfertEntreUtilisateur("refus", response,
                            idTransaction, idUser);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //transformer arraylist en String
                String afficherText = null;
                for(int i = 0; i < responseAPI.size(); i++)
                {
                    afficherText = responseAPI.get(i) + "\n";
                }

                if(afficherText != null)
                {
                    Toast.makeText(Notification.this, afficherText + "", Toast.LENGTH_LONG).show();
                }

                recreate();
                }


        });



        dialog.show();

        //Toast is call fonctionne pas

        //Si call fonctionne fermer dialog, et toast success



    }
}