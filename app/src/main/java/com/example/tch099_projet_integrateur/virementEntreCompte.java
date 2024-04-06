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

import java.util.ArrayList;
import java.util.List;

/**
 * Cette activité permet à l'utilisateur d'effectuer un virement entre ses comptes bancaires.
 */
public class virementEntreCompte extends AppCompatActivity {

    public static Utilisateur user = new Utilisateur();
    private List<CompteBancaire> lesComptes;

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte, btnDeconnexion;
    RadioGroup radioGroup_de, radioGroup_vers;
    EditText montant;
    Button BtnTransfert;

    int num_envois;
    int num_recu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virement_entre_compte);

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
        BtnTransfert = findViewById(R.id.btnTrans);
        btnDeconnexion = findViewById(R.id.btnDeconnexion);

        try {
            ArrayList<CompteBancaire> liste = ConnexionBD.getComptes(user.getId());
            user.setListeComptes(liste);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        radioGroup_de = findViewById(R.id.radioGroup_re);
        radioGroup_vers = findViewById(R.id.radioGroup_transfer);
        montant = findViewById(R.id.montant_transfert);

        lesComptes = PagePrincipale.user.getListeComptes();

        //Afficher les comptes d'envoi dans le radio group
        for (CompteBancaire compte : lesComptes) {
            RadioButton btnRadio = new RadioButton(getApplicationContext());
            btnRadio.setId(compte.getNumCompte());
            btnRadio.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_bold));

            typeCompte type = compte.getTypeCompte();
            // Ajouter le texte du compte selon le type
            switch (type) {
                case CHEQUE:
                    btnRadio.setText("Chèque");
                    break;
                case EPARGNE:
                    btnRadio.setText("Épargne");
                    break;
                case CARTE_CREDIT:
                    btnRadio.setText("Carte requin");
                    break;
                default:
                    btnRadio.setText("Investissement");
                    break;
            }

            radioGroup_de.addView(btnRadio);
        }

        //Afficher les comptes de réception dans le radio group
        for (CompteBancaire compte : lesComptes) {
            RadioButton btnRadio = new RadioButton(getApplicationContext());
            btnRadio.setId(compte.getNumCompte());
            btnRadio.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_bold));

            typeCompte type = compte.getTypeCompte();
            // Ajouter le texte du compte selon le type
            switch (type) {
                case CHEQUE:
                    btnRadio.setText("Chèque");
                    break;
                case EPARGNE:
                    btnRadio.setText("Épargne");
                    break;
                case CARTE_CREDIT:
                    btnRadio.setText("Carte requin");
                    break;
                default:
                    btnRadio.setText("Investissement");
                    break;
            }

            radioGroup_vers.addView(btnRadio);
        }

        BtnTransfert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _montant = montant.getText().toString();

                if (_montant.isEmpty()) {
                    Toast.makeText(virementEntreCompte.this, "Veuillez sélectionner un montant", Toast.LENGTH_SHORT).show();
                } else {
                    int _id_compte_envoi = 0;
                    int _id_compte_reception = 0;

                    //Itérer les boutons radio des comptes d'envoi pour voir lequel a été cliqué
                    for (int i = 0; i < radioGroup_de.getChildCount(); i++) {
                        RadioButton button = (RadioButton) radioGroup_de.getChildAt(i);
                        if (button.isChecked()) {
                            //Si le bouton est cliqué, on prend l'ID du compte en question
                            _id_compte_envoi = lesComptes.get(i).getNumCompte();
                            break;
                        }
                    }

                    //Itérer les boutons radio des comptes de réception pour voir lequel a été cliqué
                    for (int i = 0; i < radioGroup_vers.getChildCount(); i++) {
                        RadioButton button = (RadioButton) radioGroup_vers.getChildAt(i);
                        if (button.isChecked()) {
                            //Si le bouton est cliqué, on prend l'ID du compte en question
                            _id_compte_reception = lesComptes.get(i).getNumCompte();
                            break;
                        }
                    }

                    if (_id_compte_envoi == 0) {
                        Toast.makeText(virementEntreCompte.this, "Aucun compte d'envoi n'a été sélectionné", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (_id_compte_reception == 0) {
                        Toast.makeText(virementEntreCompte.this, "Aucun compte de réception n'a été sélectionné", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RecuLogin recu = null;

                    //Appel à l'API pour effectuer le transfert
                    try {
                        recu = ConnexionBD.transfertEntreComptes(PagePrincipale.user.getId(), _id_compte_envoi, _id_compte_reception, Double.parseDouble(_montant));
                    } catch (Exception e) {
                        Log.e("TAG", "Error calling API: " + e.getMessage());
                    }

                    if (recu.getCode() == 201) {
                        Toast.makeText(getApplicationContext(), "Succès! Transfert effectué.", Toast.LENGTH_SHORT).show();
                        finish();
                        redirectActivity(virementEntreCompte.this, PagePrincipale.class);
                    } else {
                        Toast.makeText(getApplicationContext(), recu.getReponse(), Toast.LENGTH_SHORT).show();
                        recreate();
                    }
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

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(virementEntreCompte.this, Notification.class);
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
