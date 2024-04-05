package com.example.tch099_projet_integrateur;
import com.example.tch099_projet_integrateur.enumerations.*;

import static com.example.tch099_projet_integrateur.PagePrincipale.openDrawer;
import static com.example.tch099_projet_integrateur.PagePrincipale.redirectActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tch099_projet_integrateur.info_user.RecuLogin;
import com.example.tch099_projet_integrateur.info_user.Utilisateur;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.io.FileNotFoundException;

public class DepotCheque extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, depot, facture, notification, support, transfertClient, transfertCompte;
    ImageView recto, verso;
    EditText montantDepot;
    Button deposer;
    Uri uriRecto;
    Uri uriVerso;

    public static Utilisateur user = new Utilisateur();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depot_cheque);
        uriRecto = null;
        uriVerso =null;
        montantDepot = findViewById(R.id.editTextMontantDepot);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        depot = findViewById(R.id.depot);
        facture = findViewById(R.id.facture);
        notification = findViewById(R.id.notification);
        support = findViewById(R.id.support);
        transfertClient = findViewById(R.id.transfertClient);
        transfertCompte = findViewById(R.id.transfertCompte);
        recto = findViewById(R.id.imgRecto);
        verso = findViewById(R.id.imgVerso);
        deposer = findViewById(R.id.buttonDepot);

        //Vérifier que la session n'est pas expirée
        PagePrincipale.verifSession(this);

        recto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 69);
            }
        });
        verso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 70);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(DepotCheque.this, Notification.class);
            }
        });


        depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(DepotCheque.this, PagePrincipale.class);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PagePrincipale.redirectActivity(DepotCheque.this, SupportNautico.class);
            }
        });

        facture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                redirectActivity(DepotCheque.this, paiementFacture.class);
            }
        });

        transfertCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(DepotCheque.this, virementEntreCompte.class);
            }
        });

        transfertClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(DepotCheque.this, virementEntreUtilisateurs.class);

            }
        });
        montantDepot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (uriRecto!=null && uriVerso!=null && montantDepot.getText().toString().trim().length()>0){
                        deposer.setEnabled(true);
                    }
                    else{
                        deposer.setEnabled(false);
                    }
                }
            }
        });

        deposer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = true;
                String messageErreur="";
                if (uriRecto == null) {
                    ok = false;
                    messageErreur+= getResources().getString(R.string.recto);
                }
                if (uriVerso == null) {
                    ok = false;
                    messageErreur+=getResources().getString(R.string.verso);
                }
                if (montantDepot.getText().toString().trim().length() == 0) {
                    ok = false;
                    messageErreur+= getResources().getString(R.string.montantManquant);
                }
                if (!ok){
                    Toast.makeText(DepotCheque.this, messageErreur, Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        Bitmap resizedBmp;
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriRecto));
                        //attention, hardcode pour la photo du cheque...
                        //Bitmap resizedBmp = Bitmap.createBitmap(bitmap,(int)(0.81315*bitmap.getWidth()),(int)(0.35389*bitmap.getHeight()),(int)(0.1050708*bitmap.getWidth()),(int)(0.07077*bitmap.getHeight()));
                        //dimension image = 2189x989
                        if (bitmap.getWidth()==2189 && bitmap.getHeight()==989){
                            resizedBmp = Bitmap.createBitmap(bitmap, 1780, 350, 230, 70);
                        }
                        else{
                            resizedBmp = bitmap;
                        }
                        InputImage image = InputImage.fromBitmap(resizedBmp, 0);
                        TextRecognizer recognizer;
                        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
                        Task<Text> result =
                                recognizer.process(image)
                                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                                            @Override
                                            public void onSuccess(Text visionText) {
                                                String resultText = visionText.getText();
                                                if (resultText.trim().length() == 0 || !isNumeric(resultText)){
                                                    Toast.makeText(DepotCheque.this, getResources().getString(R.string.photoInv), Toast.LENGTH_SHORT).show();
                                                }
                                                else if (Double.parseDouble(resultText) == Double.parseDouble(montantDepot.getText().toString())) {

                                                    //FAIRE LA REQUÊTE POUR LE DÉPÔT
                                                    double montant = Double.parseDouble(montantDepot.getText().toString());

                                                    RecuLogin resultat;

                                                    try {
                                                        resultat = ConnexionBD.depotMobile(PagePrincipale.user.getId(), montant);
                                                    } catch (InterruptedException e) {
                                                        throw new RuntimeException(e);
                                                    }

                                                    //Afficher le résultat du dépôt
                                                    if(resultat.getCode() == 201)
                                                    {
                                                        Toast.makeText(getApplicationContext(), resultat.getReponse(), Toast.LENGTH_SHORT).show();
                                                        PagePrincipale.redirectActivity(DepotCheque.this, PagePrincipale.class);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Erreur du dépot" + resultat.getCode(), Toast.LENGTH_SHORT).show();
                                                    }


                                                } else {
                                                    Toast.makeText(DepotCheque.this, getResources().getString(R.string.montantInv), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(DepotCheque.this, getResources().getString(R.string.photoInv), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 69){
            if (resultCode == RESULT_OK){
                uriRecto = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriRecto));
                    recto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {e.printStackTrace();}
            }
        }
        else if (requestCode == 70){
            if (resultCode == RESULT_OK){
                uriVerso = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriVerso));
                    verso.setImageBitmap(bitmap);
                }
                catch (FileNotFoundException e){e.printStackTrace();}
            }
        }
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}