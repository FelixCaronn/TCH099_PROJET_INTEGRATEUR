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

import java.util.List;

public class virementEntreUtilisateurs extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    RadioGroup radioGroup_de;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;
    EditText montant, email, Question, reponse, confReponse;
    Button btnTrans;

    private List<CompteBancaire> lesComptes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virement_entre_utilisateurs);

        //Vérifier que la session n'est pas expirée
        PagePrincipale.verifSession(this);

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
                redirectActivity(virementEntreUtilisateurs.this, PagePrincipale.class);
            }
        });

        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreUtilisateurs.this, DepotCheque.class);
            }
        });

        facture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreUtilisateurs.this, paiementFacture.class);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreUtilisateurs.this, SupportNautico.class);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreUtilisateurs.this, Notification.class);
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
                redirectActivity(virementEntreUtilisateurs.this, virementEntreCompte.class);
            }
        });

        //CHERCHER LES DONNÉES DU VIREMENT
        email = findViewById(R.id.email);
        Question = findViewById(R.id.Question);
        reponse = findViewById(R.id.reponse);
        confReponse = findViewById(R.id.confReponse);
        montant = findViewById(R.id.montant_virement);

        lesComptes = PagePrincipale.user.getListeComptes();
        radioGroup_de = findViewById(R.id.radioGroup_re);
        btnTrans = findViewById(R.id.btnTrans);

        //Afficher les comptes d'envoie dans le radio group
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

            radioGroup_de.addView(btnRadio);
        }

        btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _montant = montant.getText().toString();

                if (_montant.isEmpty())
                {
                    Toast.makeText(virementEntreUtilisateurs.this, "Veuillez inscrire un montant", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int idCompteBancaireProvenant = 0;

                    Log.e("TAG", lesComptes.get(0).toString());


                    //Itérer les boutons radio des comptes d'envoie pour voir lequel a été cliqué
                    for (int i = 0; i < radioGroup_de.getChildCount(); i++) {
                        RadioButton button = (RadioButton) radioGroup_de.getChildAt(i);

                        if (button.isChecked()) {
                            //Si le bouton est cliqué, on prend l'ID du compte en question
                            idCompteBancaireProvenant = lesComptes.get(i).getNumCompte();

                            break;
                        }
                    }


                    if (idCompteBancaireProvenant == 0){
                        Toast.makeText(virementEntreUtilisateurs.this, "Veuillez sélectionner un compte", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Chercher le reste des infos


                    //ON APPELLE L'API
                    RecuLogin recu = null;
                    try
                    {
                        recu = ConnexionBD.virementEntrePersonnes(PagePrincipale.user.getId(),
                                idCompteBancaireProvenant, Double.parseDouble(_montant), email.getText().toString(),
                                Question.getText().toString(), reponse.getText().toString(), confReponse.getText().toString());
                    }
                    catch (Exception e)
                    {
                        Log.e("TAG", "Error calling API: " + e.getMessage());
                    }

                    if (recu.getCode() == 201)
                    {
                        Toast.makeText(getApplicationContext(), "Succès! Virement effectué.", Toast.LENGTH_SHORT).show();
                        finish();
                        redirectActivity(virementEntreUtilisateurs.this, PagePrincipale.class);
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
