package com.example.tch099_projet_integrateur;

import android.os.StrictMode;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBD extends Thread{

    public static boolean connexion(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/application", "admin", "admin");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return true;
    }




}
