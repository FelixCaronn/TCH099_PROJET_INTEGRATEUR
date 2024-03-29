package com.example.tch099_projet_integrateur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.tch099_projet_integrateur.enumerations.typeCompte;
import com.example.tch099_projet_integrateur.info_user.CompteBancaire;
import com.example.tch099_projet_integrateur.info_user.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class virementEntreCompte extends AppCompatActivity {


    EditText montant;
    public static Utilisateur user = new Utilisateur();
    private List<CompteBancaire> lesComptes;

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte ;
    Button BtnTransfert;

    int num_envois;
    int num_recu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virement_entre_compte);
        drawerLayout = findViewById(R.id.drawerLayout);

        menu = findViewById(R.id.menu);

        home = findViewById(R.id.home);
        depot = findViewById(R.id.depot);
        facture = findViewById(R.id.facture);
        notification = findViewById(R.id.notification);
        support = findViewById(R.id.support);
        transfertClient = findViewById(R.id.transfertClient);
        transfertCompte = findViewById(R.id.transfertCompte);
        BtnTransfert = findViewById(R.id.btnTrans);

        try {
            ArrayList<CompteBancaire> liste = ConnexionBD.getComptes(user.getId());
            user.setListeComptes(liste);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




        BtnTransfert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sélectionne le bouton sélectioné du premier groupe de bouton
                RadioGroup radioEnvois = (RadioGroup) findViewById(R.id.radioGroup_re);
                int selectedId = radioEnvois.getCheckedRadioButtonId();
                RadioButton buttonEnvois = (RadioButton) findViewById(selectedId);
                lesComptes = user.getListeComptes();
                //on sélectionne le compte de l'utilisateur
                if(buttonEnvois.getText() == "Compte chèque"){

                    //le premier compte est le compte cheque


                   num_envois = lesComptes.get(0).getNumCompte();

                }
                else{
                        //sinon on cherche quel compte dans la liste est le compte épargne
                    int tempo =0;

                    for (int i=1; i<lesComptes.size()-1; i++ ){

                        if(lesComptes.get(i).getTypeCompte() == typeCompte.EPARGNE){
                             tempo = i;
                        }
                    }
                    num_envois = lesComptes.get(tempo).getNumCompte();
                }
                RadioGroup radioRecois = (RadioGroup) findViewById(R.id.radioGroup_transfer);
                int selectedId2 = radioRecois.getCheckedRadioButtonId();
                RadioButton buttonRecois = (RadioButton) findViewById(selectedId2);

                int tempo2 =0;

                if(buttonRecois.getText() == "Compte chèque"){
                    num_recu = lesComptes.get(0).getNumCompte();
                } else if (buttonRecois.getText() == "compte épargne") {

                    for (int i=1; i<=2; i++ ){

                        if(lesComptes.get(i).getTypeCompte() == typeCompte.EPARGNE){
                            tempo2 = i;
                        }
                    }
                    
                }else{
                    for (int i=1; i<=2; i++ ){

                        if(lesComptes.get(i).getTypeCompte() == typeCompte.CARTE_CREDIT){
                            tempo2 = i;
                        }
                    }
                }

                num_recu = tempo2;

                montant = findViewById(R.id.montant_transfert);

                double montantFormater = Double.parseDouble(montant.getText().toString());

                try {
                    ConnexionBD.transfertEntreComptes(num_envois, num_recu, montantFormater);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
        });




        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreCompte.this, PagePrincipale.class);
            }
        });

        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreCompte.this, DepotCheque.class);
            }
        });




        facture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreCompte.this, paiementFacture.class);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreCompte.this, SupportNautico.class);
            }
        });

        transfertClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreCompte.this, virementEntreUtilisateurs.class);

            }
        });

        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
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

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }



}
