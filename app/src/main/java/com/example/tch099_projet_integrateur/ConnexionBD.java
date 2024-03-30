package com.example.tch099_projet_integrateur;
import com.example.tch099_projet_integrateur.enumerations.typeCompte;
import com.example.tch099_projet_integrateur.enumerations.typeTransaction;
import com.example.tch099_projet_integrateur.info_user.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ConnexionBD extends Thread{

    private ProgressDialog processDialog;




    private JSONArray resultJson;
    private int success=0;

    private ListView listView;





    


    //Adresses des API du site web qu'on utilise pour get/post nos données
    private static final String apiPathVerifLogin = "http://35.233.243.199/TCH099_FishFric/Site_web/Connexion/API/apiConnexion.php";
    private static final String apiPathCreationCompte = "http://35.233.243.199/TCH099_FishFric/Site_web/Creer_un_compte/API/apiCreerCompte.php";
    private static final String apiPathListeComptes = "http://35.233.243.199/TCH099_FishFric/Site_web/Liste_compte/API/afficherComptes.php";
    private static final String apiPathDepotMobile = "http://35.233.243.199/TCH099_FishFric/Site_web/Transfert/API/depotMobile.php";
    private static final String apiPathListeTransaction = "http://35.233.243.199/TCH099_FishFric/Site_web/consulterCompte/API/getCompte.php";
    private static final String apiPathTransfertComptes = "http://35.233.243.199/TCH099_FishFric/Site_web/Transfert/API/gestionTransfertmobile.php/compte";
    private static final String apiPathPayerFacture = "http://35.233.243.199/TCH099_FishFric/Site_web/Transfert/API/gestionTransfertmobile.php/facture";

    public static RecuLogin verifLogin(String username, String mdp) throws InterruptedException {


        RecuLogin verifLog = new RecuLogin();

        Thread p = new Thread()
        {

            @Override
            public void run() {



                OkHttpClient client = new OkHttpClient();


                JSONObject postData = new JSONObject();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    try {
                        postData.append("courriel", username);
                        postData.append("password", mdp);
                        postData.append("mobile", 1);
                        postData.append("checked",false);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }

                final MediaType JSON = MediaType.parse("application/json, charset=utf-8");
                RequestBody postBody = RequestBody.create(JSON, postData.toString());
                Request post = new Request.Builder()
                        .url(apiPathVerifLogin)
                        .post(postBody)
                        .build();


                try(Response response = client.newCall(post).execute())
                {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response.code());

                    ResponseBody responseBody = response.body();
                    ObjectMapper mapper = new ObjectMapper();


                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();
                    if(codeRes.equals("200") || codeRes.equals("201"))
                    {
                        String userNom = json.get("nom").asText();
                        int userId = json.get("id").asInt();
                        verifLog.setId(userId);
                        verifLog.setNom(userNom);
                    }

                    int code = Integer.parseInt(codeRes);
                    verifLog.setCode(code);
                    verifLog.setReponse(reponse);



                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                currentThread().interrupt();

            }
        };

        p.start();
        p.join();

        return verifLog;

    }

    //***************************** REQUÊTE CRÉER COMPTE UTILISATEUR *******************************//

    public static RecuLogin creationCompte(String nom, String prenom, String courriel,String mdp, String confirmationMdp) throws InterruptedException {

        RecuLogin recu = new RecuLogin();
        Thread p = new Thread(){


            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                JSONObject postData = new JSONObject();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    try {
                        postData.append("nom", nom);
                        postData.append("prenom", prenom);
                        postData.append("courriel", courriel);
                        postData.append("password", mdp);
                        postData.append("conf_password", confirmationMdp);
                        postData.append("mobile", true);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }


                final MediaType JSON = MediaType.parse("application/json, charset=utf-8");
                RequestBody postBody = RequestBody.create(JSON, postData.toString());
                Request post = new Request.Builder()
                        .url(apiPathCreationCompte)
                        .post(postBody)
                        .build();

                try(Response response = client.newCall(post).execute()){

                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    ResponseBody responseBody = response.body();
                    ObjectMapper mapper = new ObjectMapper();

                    //Recuperer resultat requete API
                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();
                    int code = Integer.parseInt(codeRes);
                    recu.setCode(code);
                    recu.setReponse(reponse);


                }catch (Exception e)
                {
                    Log.e("TAG", "Error calling API code : 404");
                }

            }
        };

        p.start();
        p.join();

        return recu;
    }

//******************************** REQUÊTE GET COMPTES ***********************************//

    public static ArrayList<CompteBancaire> getComptes(int id) throws InterruptedException {

        Utilisateur u = new Utilisateur();

        Thread p = new Thread(){

            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                JSONObject getData = new JSONObject();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    try{

                        getData.append("utilisateur", id);

                    }catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                }

                final MediaType JSON = MediaType.parse("application/json, charset=utf-8");
                RequestBody getBody = RequestBody.create(JSON, getData.toString());
                Request get = new Request.Builder()
                        .url(apiPathListeComptes)
                        .post(getBody)
                        .build();

                try(Response response = client.newCall(get).execute())
                {

                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    final ObjectMapper mapper = new ObjectMapper();

                    JSONObject obj = new JSONObject(response.body().string());

                    JSONArray jsonArray = obj.getJSONArray("comptes");




                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject tmp = jsonArray.getJSONObject(i);
                        typeCompte type=null;
                        switch(tmp.getString("typeCompte"))
                        {
                            case "Compte chèque":
                                type = typeCompte.CHEQUE;
                                break;
                            case "Compte investissement":
                                type = typeCompte.INVESTISSEMENT;
                                break;
                            case "Carte Requin":
                                type = typeCompte.CARTE_CREDIT;
                                break;
                            case "Compte épargne":
                                type = typeCompte.EPARGNE;
                                break;
                            default:
                                type = null;
                                break;
                        }
                        CompteBancaire tmp1 = new CompteBancaire(tmp.getInt("id"), tmp.getDouble("solde"), type);
                        u.addCompte(tmp1);
                        //Log.e("TAG", "Num:"+tmp1.getNumCompte()
                        //+"\n"+"Solde :"+tmp1.getSolde() + "\n" + "Type: " + tmp1.getTypeCompte());


                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                currentThread().interrupt();

            }
        };

        p.start();
        p.join();

        return u.getListeComptes();
    }

//******************************** REQUÊTE DÉPÔT MOBILE ***********************************//

    public static RecuLogin depotMobile(int idUtilisateur, double montant) throws InterruptedException {

        RecuLogin recu = new RecuLogin();
        Thread p = new Thread(){


            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Log.e("ID ET MONTANT DANS LA FCT", String.valueOf(idUtilisateur + montant));

                //Ajouter les données pour le dépôt
                JSONObject postData = new JSONObject();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    try {
                        postData.append("idUtilisateur", idUtilisateur);
                        postData.append("montant", montant);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                //Faire la requête
                final MediaType JSON = MediaType.parse("application/json, charset=utf-8");
                RequestBody postBody = RequestBody.create(JSON, postData.toString());
                Request put = new Request.Builder()
                        .url(apiPathDepotMobile)
                        .put(postBody)
                        .build();

                try(Response response = client.newCall(put).execute()){
                    //Lancer une exception s'il y a une erreur
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    //Sinon, get nos données
                    ResponseBody responseBody = response.body();
                    ObjectMapper mapper = new ObjectMapper();

                    //Recuperer resultat requete API
                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();
                    int code = Integer.parseInt(codeRes);
                    recu.setCode(code);
                    recu.setReponse(reponse);
                }
                
                catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                    Log.e("TAG", "Error calling API code : 404");

                }

            }
        };

        p.start();
        p.join();
        return recu;
    }



    public static ArrayList<TransactionBancaire> getTransaction(int id_compte) throws InterruptedException {
        CompteBancaire cpt = new CompteBancaire();

        Thread p = new Thread(){

            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                JSONObject getData = new JSONObject();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    try{

                        getData.append("compteId", id_compte);

                    }catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                }


                final MediaType JSON = MediaType.parse("application/json, charset=utf-8");
                RequestBody getBody = RequestBody.create(JSON, getData.toString());
                Request get = new Request.Builder()
                        .url(apiPathListeTransaction)
                        .post(getBody)
                        .build();

                try(Response response = client.newCall(get).execute())
                {

                    if(!response.isSuccessful()) throw new IOException("Erreur inattendue code: " + response.code());

                    final ObjectMapper mapper = new ObjectMapper();


                    JSONObject obj = new JSONObject(response.body().string());

                    JSONArray jsonArray = obj.getJSONArray("transactions");



                    for(int i = jsonArray.length()-1; i >= 0; i--)
                    {
                        JSONObject tmp = jsonArray.getJSONObject(i);
                        typeTransaction type=null;

                        switch (tmp.getString("typeTransaction"))
                        {
                            case "Transfert":
                                type = typeTransaction.TRANSFERT;
                                break;
                            case "Virement":
                                type = typeTransaction.VIREMENT;
                                break;
                            case "Virement refusé":
                                type = typeTransaction.VIREMENT_REFUSE;
                                break;
                            case "Paiement de facture":
                                type = typeTransaction.PAIEMENTFACTURE;
                                break;
                            case "Dépôt mobile":
                                type = typeTransaction.DEPOT;
                                break;
                        }

                        TransactionBancaire transactionTemp;

                        if(type == typeTransaction.TRANSFERT)
                        {
                            transactionTemp = new TransactionBancaire(type, tmp.getString("idCompteBancaireProvenant"), tmp.getString("dateTransaction"), tmp.getDouble("montant"));

                            //Si l'ID de notre compte est le même que le compte provenant dans la transaction, le transfert sort de ce compte
                            if(id_compte == tmp.getInt("idCompteBancaireProvenant")) {
                                transactionTemp.setMontant(-1 * tmp.getDouble("montant"));
                                transactionTemp.setIdCompteRecevant(tmp.getInt("idCompteBancaireRecevant"));
                            }

                            //Sinon, c'est de l'argent rentrant
                            else {
                                transactionTemp.setMontant(tmp.getDouble("montant"));
                                transactionTemp.setCompteProvenance(tmp.getInt("idCompteBancaireProvenant"));
                            }
                        }

                        else if(type == typeTransaction.DEPOT)
                        {
                            transactionTemp = new TransactionBancaire(type, tmp.getString("dateTransaction"), tmp.getDouble("montant"));
                        }


                        else if(type == typeTransaction.PAIEMENTFACTURE)
                        {
                            transactionTemp = new TransactionBancaire(type, tmp.getString("nomEtablissement"), tmp.getString("dateTransaction"), tmp.getDouble("montant"));
                            transactionTemp.setMontant(-1*tmp.getDouble("montant"));
                        }

                        //Virement
                        else
                        {
                            if(id_compte == tmp.getInt("idCompteBancaireProvenant"))
                            {
                                transactionTemp = new TransactionBancaire(type, tmp.getString("nomEtablissement"));
                                transactionTemp.setMontant(-1*tmp.getDouble("montant"));
                                transactionTemp.setDateDeTransaction(tmp.getString("dateTransaction"));
                                transactionTemp.setIdCompteRecevant(tmp.getInt("idCompteBancaireRecevant"));
                            }
                            else
                            {
                                transactionTemp = new TransactionBancaire(type, tmp.getString("nomEtablissement"));
                                transactionTemp.setMontant(tmp.getDouble("montant"));
                                transactionTemp.setProvenance(tmp.getString("courrielProvenant"));
                                transactionTemp.setDateDeTransaction(tmp.getString("dateTransaction"));
                                transactionTemp.setCompteProvenance(tmp.getInt("idCompteBancaireProvenant"));
                            }
                        }

                        cpt.addTransaction(transactionTemp);
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }

                Log.e("TAG", cpt.getListeTransactions().size() + "");

                currentThread().interrupt();
            }

        };

        p.start();
        p.join();

        return cpt.getListeTransactions();
    }


    public static RecuLogin transfertEntreComptes(int idUtilisateur, int id_comptes_envoie, int id_compte_reception, double montant) throws InterruptedException {

        RecuLogin verifLog = new RecuLogin();

        Thread p = new Thread() {

            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                JSONObject putData = new JSONObject();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    try {
                        putData.append("idUtilisateur", idUtilisateur);
                        putData.append("idCompteBancaireProvenant", id_comptes_envoie);
                        putData.append("idCompteBancaireRecevant", id_compte_reception);
                        putData.append("montant", montant);

                    } catch (JSONException e) {

                        throw new RuntimeException(e);
                    }
                }

                final MediaType JSON = MediaType.parse("application/json, charset=utf-8");
                RequestBody putBody = RequestBody.create(JSON, putData.toString());
                Request post = new Request.Builder()
                        .url(apiPathTransfertComptes)
                        .put(putBody)
                        .build();


                try (Response response = client.newCall(post).execute()) {

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    ResponseBody responseBody = response.body();
                    ObjectMapper mapper = new ObjectMapper();


                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();

                    verifLog.setReponse(reponse);
                    verifLog.setCode(Integer.parseInt(codeRes));


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                currentThread().interrupt();
            }
        };

        p.start();
        p.join();

        return verifLog;
    }

    public static RecuLogin effectuerPaiement ( int idUtilisateur, int id_compte, String etab, String num, double montant) throws InterruptedException {

        RecuLogin recu = new RecuLogin();

        Thread p = new Thread() {

            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                JSONObject paiementData = new JSONObject();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    try {
                        paiementData.append("idUtilisateur", idUtilisateur);
                        paiementData.append("idCompteBancaireProvenant", id_compte);
                        paiementData.append("nomEtablissement", etab);
                        paiementData.append("raison", num);
                        paiementData.append("montant", montant);

                    } catch (JSONException e) {

                        throw new RuntimeException(e);
                    }
                }

                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody putBody = RequestBody.create(JSON, paiementData.toString());
                Request put = new Request.Builder()
                        .url(apiPathPayerFacture)
                        .put(putBody)
                        .build();


                try (Response response = client.newCall(put).execute()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    ResponseBody responseBody = response.body();
                    ObjectMapper mapper = new ObjectMapper();

                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();

                    recu.setReponse(reponse);
                    recu.setCode(Integer.parseInt(codeRes));


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                currentThread().interrupt();
            }
        };

        p.start();
        p.join();

        return recu;
    }
}
