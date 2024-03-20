package com.example.tch099_projet_integrateur;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.mariadb.jdbc.Driver;

import java.sql.*;

public class PageAcceuil extends AppCompatActivity {

    Button btnConnexion;
    Button btnCreer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_acceuil);


        btnConnexion = findViewById(R.id.btnConnexion);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(PageAcceuil.this, PageConnection.class);
            }
        });

        btnCreer = findViewById(R.id.btnCreer);
        btnCreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(PageAcceuil.this, CreerCompte.class);
            }
        });

    }
}