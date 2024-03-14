package com.example.tch099_projet_integrateur;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

public class PagePrincipale extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_principale);


        //Active la barre de navigation
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);


        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);

        MenuItem support = menu.findItem(R.id.menuSupport);

        MenuItem virementCompte = findViewById(R.id.compte);
        MenuItem depot = findViewById(R.id.menuDepot);
        MenuItem facture = findViewById(R.id.menuFacture);
        MenuItem notification = findViewById(R.id.menuNotification);


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i("MesMessages", "YUP");

        if (item.getItemId() == R.id.menuSupport) {
            Intent s = new Intent(this, SupportNautico.class);
            startActivity(s);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}