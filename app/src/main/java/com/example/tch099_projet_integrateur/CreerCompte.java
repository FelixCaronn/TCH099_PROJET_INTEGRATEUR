package com.example.tch099_projet_integrateur;

import com.example.tch099_projet_integrateur.enumerations.*;
import com.example.tch099_projet_integrateur.info_user.RecuLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activité permettant de créer un nouveau compte utilisateur.
 */
public class CreerCompte extends AppCompatActivity implements View.OnClickListener{

    EditText nom;
    EditText prenom;
    EditText courriel;
    EditText mdp;
    EditText confirmationMdp;
    Button btnCreer;

    /**
     * Méthode exécutée lors de la création de l'activité.
     * @param savedInstanceState État enregistré de l'activité.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_compte);


        nom = findViewById(R.id.editTextNomCreer);
        prenom = findViewById(R.id.editTextPrenomCreer);
        courriel = findViewById(R.id.editTextCourrielCreer);
        mdp = findViewById(R.id.editTextMdpCreer);
        confirmationMdp = findViewById(R.id.editTextMdpConfirmation);
        btnCreer = findViewById(R.id.btnCreationCompte);

        btnCreer.setOnClickListener(this);

    }

    /**
     * Méthode appelée lorsqu'un bouton est cliqué.
     *
     */
    @Override
    public void onClick(View v) {

        String _nom = nom.getText().toString();
        String _prenom = prenom.getText().toString();
        String _courriel = courriel.getText().toString();
        String _mdp = mdp.getText().toString();
        String confirmation = confirmationMdp.getText().toString();

        RecuLogin resultat;
        try {
            resultat = ConnexionBD.creationCompte(_nom, _prenom, _courriel, _mdp, confirmation);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(resultat.getCode() == 201)
        {
            Toast.makeText(this, resultat.getReponse(), Toast.LENGTH_SHORT).show();
            PagePrincipale.redirectActivity(this, PageConnection.class);
        }
        else
        {
            Toast.makeText(this, resultat.getReponse(), Toast.LENGTH_SHORT).show();
            recreate();
        }

    }
}
