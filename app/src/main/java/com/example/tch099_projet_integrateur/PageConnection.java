package com.example.tch099_projet_integrateur;
import com.example.tch099_projet_integrateur.enumerations.*;
import com.example.tch099_projet_integrateur.info_user.RecuLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.ProgressDialog;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PageConnection extends AppCompatActivity implements View.OnClickListener {

    EditText courriel;
    EditText motdepasse;
    Button btnSoumettre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_connection);

        courriel = findViewById(R.id.editTextCourriel);
        motdepasse = findViewById(R.id.editTextMdp);
        btnSoumettre = findViewById(R.id.btnLogin);
        btnSoumettre.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {

        String mail = courriel.getText().toString();
        String mdp = motdepasse.getText().toString();

        RecuLogin resultat;
        try {
            resultat = ConnexionBD.verifLogin(mail,mdp);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(resultat.getCode() == 200)
        {
            Toast.makeText(this, resultat.getReponse(), Toast.LENGTH_SHORT).show();
            PagePrincipale.user.setNom(resultat.getNom());
            PagePrincipale.user.setId(resultat.getId());
            PagePrincipale.redirectActivity(this, PagePrincipale.class);

            //Ajuster la date de fin de la session
            PagePrincipale.calendrier.getTime();
            PagePrincipale.calendrier.add(Calendar.SECOND, 100);
            PagePrincipale.endTime = PagePrincipale.calendrier.getTime();

        }
        else
        {
            Toast.makeText(this, resultat.getReponse(), Toast.LENGTH_SHORT).show();
            recreate();
        }

    }


}