package com.example.tch099_projet_integrateur;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tch099_projet_integrateur.enumerations.typeCompte;
import com.example.tch099_projet_integrateur.info_user.CompteBancaire;
import com.example.tch099_projet_integrateur.info_user.RecuLogin;
import com.example.tch099_projet_integrateur.info_user.Utilisateur;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class paiementFacture extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;
    private List<CompteBancaire> lesComptes;
    RadioGroup radioGroup;
    EditText etablissement, num_ref, montant_facture;
    Button btnTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiement_facture);

        //Vérifier que la session n'est pas expirée
        PagePrincipale.verifSession(this);

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
                redirectActivity(paiementFacture.this, PagePrincipale.class);
            }
        });

        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(paiementFacture.this, DepotCheque.class);
            }
        });

        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(paiementFacture.this, virementEntreCompte.class);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(paiementFacture.this, Notification.class);
            }
        });


        facture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(paiementFacture.this, SupportNautico.class);
            }
        });

        transfertClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redirectActivity(paiementFacture.this, virementEntreUtilisateurs.class);

            }
        });

        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                redirectActivity(paiementFacture.this, virementEntreCompte.class);

            }
        });

        radioGroup = findViewById(R.id.radioGroup_re);

        etablissement = findViewById(R.id.etablissement);
        num_ref = findViewById(R.id.num_ref);
        montant_facture = findViewById(R.id.montant_facture);
        btnTrans = findViewById(R.id.btnTrans);


        lesComptes = PagePrincipale.user.getListeComptes();

        //Afficher les comptes dans le radio group
        for (CompteBancaire compte : lesComptes) {
            //Créer un bouton radio et mettre l'ID de compte comme ID
            RadioButton btnRadio = new RadioButton(getApplicationContext());
            btnRadio.setId(compte.getNumCompte());
            btnRadio.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_bold));

            //Mettre le texte du compte selon le titre
            typeCompte type = compte.getTypeCompte();

            if (type == typeCompte.CHEQUE)
                btnRadio.setText("Chèque");
            else if (type == typeCompte.EPARGNE)
                btnRadio.setText("Épargne");
            else if (type == typeCompte.CARTE_CREDIT)
                btnRadio.setText("Carte requin");
            else
                btnRadio.setText("Investissement");


            radioGroup.addView(btnRadio);
        }

        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _etab = etablissement.getText().toString();
                String _num = num_ref.getText().toString();
                String _montant = montant_facture.getText().toString();

                if (_etab.isEmpty() || _num.isEmpty() || _montant.isEmpty())
                {
                    Toast.makeText(paiementFacture.this, "Veuillez remplir toutes les informations", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int _id_compte = 0;

                    Log.e("TAG", lesComptes.get(0).toString());


                    //Itérer les boutons radio pour voir lequel a été cliqué
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        RadioButton button = (RadioButton) radioGroup.getChildAt(i);

                        if (button.isChecked()) {
                            //Si le bouton est cliqué, on prend l'ID du compte en question
                            _id_compte = lesComptes.get(i).getNumCompte();

                            break;
                        }
                    }


                    if (_id_compte == 0){
                        Toast.makeText(paiementFacture.this, "Aucun compte n'a été selectionné", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RecuLogin recu = null;

                    //On appelle l'API pour tester
                    try
                    {
                        recu = ConnexionBD.effectuerPaiement(PagePrincipale.user.getId(), _id_compte, _etab, _num, Double.parseDouble(_montant));
                    }
                    catch (Exception e)
                    {
                        Log.e("TAG", "Error calling API: " + e.getMessage());
                    }

                    if (recu.getCode() == 201)
                    {
                        Toast.makeText(getApplicationContext(), "Succès! Paiement effectué.", Toast.LENGTH_SHORT).show();
                        finish();
                        redirectActivity(paiementFacture.this, PagePrincipale.class);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), recu.getReponse(), Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                }
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