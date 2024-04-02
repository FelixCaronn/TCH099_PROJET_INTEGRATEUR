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

    //Adresses des API du site web qu'on utilise pour get/post nos données
    private static final String apiPathVerifLogin = "http://35.233.243.199/TCH099_FishFric/Site_web/Connexion/API/apiConnexion.php";
    private static final String apiPathCreationCompte = "http://35.233.243.199/TCH099_FishFric/Site_web/Creer_un_compte/API/apiCreerCompte.php";
    private static final String apiPathListeComptes = "http://35.233.243.199/TCH099_FishFric/Site_web/Liste_compte/API/afficherComptes.php";
    private static final String apiPathDepotMobile = "http://35.233.243.199/TCH099_FishFric/Site_web/Transfert/API/depotMobile.php";
    private static final String apiPathListeTransaction = "http://35.233.243.199/TCH099_FishFric/Site_web/consulterCompte/API/getCompte.php";
    private static final String apiPathTransfertComptes = "http://35.233.243.199/TCH099_FishFric/Site_web/Transfert/API/gestionTransfertmobile.php/compte";
    private static final String apiPathPayerFacture = "http://35.233.243.199/TCH099_FishFric/Site_web/Transfert/API/gestionTransfertmobile.php/facture";
    private static final String apiPathVirementPersonnes = "http://35.233.243.199/TCH099_FishFric/Site_web/Transfert/API/gestionTransfertmobile.php/utilisateurEnvoi";
    private static final String apiPathVirementPersonnesReception = "http://35.233.243.199/TCH099_FishFric/Site_web/Transfert/API/gestionTransfertmobile.php/utilisateurReception";
    private static final String apiPathGetNotifications = "http://35.233.243.199/TCH099_FishFric/Site_web/Liste_compte/API/afficherNotificationsMobile.php";

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

                //Faire la requête pour vérifier les données du login
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

                    //Chercher la réponse et le code retourné par l'API
                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();

                    //Si le code est 200 ou 201, le login est correct
                    if(codeRes.equals("200") || codeRes.equals("201"))
                    {
                        //Stocker l'ID et le nom de l'utilisateur connecté
                        String userNom = json.get("nom").asText();
                        int userId = json.get("id").asInt();
                        verifLog.setId(userId);
                        verifLog.setNom(userNom);
                    }

                    //Mettre le code et la réponse dans le login
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
                        //Ajouter les données d'informations sur le client qu'on veut créer
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

                //Envoyer la requête avec les données
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

                    //Récupérer le résultat que retourne l'API
                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();

                    //Stocker le résultat de l'API et le retourner à la fin
                    int code = Integer.parseInt(codeRes);
                    recu.setCode(code);
                    recu.setReponse(reponse);

                } catch (Exception e) {
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
                //Créer le client de la requête et l'objet JSON que l'on va envoyer
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

                //Faire la requête
                final MediaType JSON = MediaType.parse("application/json, charset=utf-8");
                RequestBody getBody = RequestBody.create(JSON, getData.toString());
                Request get = new Request.Builder()
                        .url(apiPathListeComptes)
                        .post(getBody)
                        .build();

                //Exécuter la requête et prendre la réponse de l'API
                try(Response response = client.newCall(get).execute())
                {

                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    final ObjectMapper mapper = new ObjectMapper();

                    //Chercher le tableau de comptes
                    JSONObject obj = new JSONObject(response.body().string());
                    JSONArray jsonArray = obj.getJSONArray("comptes");

                    //Itérer les comptes de l'utilisateur
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        //Créer un objet JSON pour chaque compte
                        JSONObject tmp = jsonArray.getJSONObject(i);
                        typeCompte type = null;

                        //Assigner le type de compte pour chaque compte selon la réponse de l'API
                        switch(tmp.getString("typeCompte"))
                        {
                            case "Compte chèque":
                                type = typeCompte.CHEQUE;
                                break;
                            case "Compte investissement":
                                type = typeCompte.INVESTISSEMENT;
                                break;
                            case "Carte requin":
                                type = typeCompte.CARTE_CREDIT;
                                break;
                            case "Compte épargne":
                                type = typeCompte.EPARGNE;
                                break;
                            default:
                                type = null;
                                break;
                        }

                        //Créer un compte bancaire et l'ajouter à la liste des comptes de l'utilisateur
                        CompteBancaire tmp1 = new CompteBancaire(tmp.getInt("id"), tmp.getDouble("solde"), type);
                        u.addCompte(tmp1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                currentThread().interrupt();
            }
        };

        p.start();
        p.join();

        //Retourner la liste des comptes de l'utilisateur
        return u.getListeComptes();
    }

//******************************** REQUÊTE DÉPÔT MOBILE ***********************************//

    public static RecuLogin depotMobile(int idUtilisateur, double montant) throws InterruptedException {

        RecuLogin recu = new RecuLogin();
        Thread p = new Thread(){


            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

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

                    //Récupérer résultat de l'API
                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();

                    //Set les valeurs reçues de l'API dans la variable reçu, qu'on va retourner
                    int code = Integer.parseInt(codeRes);
                    recu.setCode(code);
                    recu.setReponse(reponse);
                }
                
                catch (Exception e) {
                    Log.e("TAG", e.getMessage());
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
                        //Envoyer l'ID du compte à l'API pour nous retourner la liste des transactions de ce compte
                        getData.append("compteId", id_compte);
                    } catch(Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                //Faire la requête à l'API
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

                    //Itérer les transactions (à l'envers pour les avoir dans l'ordre chronologique décroissant
                    for(int i = jsonArray.length()-1; i >= 0; i--)
                    {
                        JSONObject tmp = jsonArray.getJSONObject(i);
                        typeTransaction type=null;

                        //Set le type de la transaction selon le type renvoyé par l'API
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

                        //Créer les transaction et set leurs valeurs selon le type de transaction
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

                        else if(type == typeTransaction.DEPOT) {
                            transactionTemp = new TransactionBancaire(type, tmp.getString("dateTransaction"), tmp.getDouble("montant"));
                        }


                        else if(type == typeTransaction.PAIEMENTFACTURE) {
                            //C'est de l'argent sortant, on set donc un montant négatif, la date et le nom de l'établissement
                            transactionTemp = new TransactionBancaire(type, tmp.getString("nomEtablissement"), tmp.getString("dateTransaction"), tmp.getDouble("montant"));
                            transactionTemp.setMontant(-1*tmp.getDouble("montant"));
                        }

                        //Virement entre personnes
                        else if (type == typeTransaction.VIREMENT)
                        {
                            if(id_compte == tmp.getInt("idCompteBancaireProvenant"))
                            {
                                transactionTemp = new TransactionBancaire(type, tmp.getString("nomEtablissement"));
                                transactionTemp.setMontant(-1*tmp.getDouble("montant"));
                                transactionTemp.setDateDeTransaction(tmp.getString("dateTransaction"));
                            }
                            else
                            {
                                transactionTemp = new TransactionBancaire(type, tmp.getString("nomEtablissement"));
                                transactionTemp.setMontant(tmp.getDouble("montant"));
                                transactionTemp.setProvenance(tmp.getString("courrielProvenant"));
                                transactionTemp.setDateDeTransaction(tmp.getString("dateTransaction"));
                            }
                        }

                        //Virement refusé
                        else  {
                            transactionTemp = new TransactionBancaire(type, tmp.getString("nomEtablissement"));
                            transactionTemp.setMontant(tmp.getDouble("montant"));
                            transactionTemp.setProvenance(tmp.getString("nomEtablissement"));
                            transactionTemp.setDateDeTransaction(tmp.getString("dateTransaction"));
                        }

                        //Ajouter la transaction à la liste des transaction de ce compte
                        cpt.addTransaction(transactionTemp);
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }

                currentThread().interrupt();
            }

        };

        p.start();
        p.join();

        //retourner la liste des transactions
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
                        //Envoyer l'ID de l'utilisateur, l'ID des 2 comptes bancaires et le montant transféré
                        putData.append("idUtilisateur", idUtilisateur);
                        putData.append("idCompteBancaireProvenant", id_comptes_envoie);
                        putData.append("idCompteBancaireRecevant", id_compte_reception);
                        putData.append("montant", montant);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                //Faire la requête
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

                    //Lire la réponse de l'API
                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();

                    //Stocker le code et la réponse de l'API, et renvoyer ça
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

        //On retourne la réponse de l'API
        return verifLog;
    }


    public static RecuLogin virementEntrePersonnes(int idUtilisateur, int idCompteBancaireProvenant, double montant, String courrielDest, String question, String reponse, String confReponse) throws InterruptedException {

        RecuLogin verifLog = new RecuLogin();

        Thread p = new Thread() {

            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                JSONObject putData = new JSONObject();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    try {
                        //Envoyer toutes les données du virement
                        putData.append("idUtilisateur", idUtilisateur);
                        putData.append("idCompteBancaireProvenant", idCompteBancaireProvenant);
                        putData.append("montant", montant);
                        putData.append("courrielDest", courrielDest);
                        putData.append("question", question);
                        putData.append("reponse", reponse);
                        putData.append("confReponse", confReponse);


                    } catch (JSONException e) {

                        throw new RuntimeException(e);
                    }
                }

                //Faire la requête
                final MediaType JSON = MediaType.parse("application/json, charset=utf-8");
                RequestBody putBody = RequestBody.create(JSON, putData.toString());
                Request post = new Request.Builder()
                        .url(apiPathVirementPersonnes)
                        .put(putBody)
                        .build();


                try (Response response = client.newCall(post).execute()) {

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    ResponseBody responseBody = response.body();
                    ObjectMapper mapper = new ObjectMapper();

                    //Stocker la réponse de l'API
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
                        //Envoyer toutes les données du paiement de facture
                        paiementData.append("idUtilisateur", idUtilisateur);
                        paiementData.append("idCompteBancaireProvenant", id_compte);
                        paiementData.append("nomEtablissement", etab);
                        paiementData.append("raison", num);
                        paiementData.append("montant", montant);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                //Faire la requête
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

                    //Récupérer la réponse de l'API
                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();

                    //Stocker la réponse
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








    public static ArrayList<Notifications> getNotifications (int idUtilisateur) throws InterruptedException {

        //Créer une arrayList de notifications, qu'on va retourner à la fin
        ArrayList<Notifications> listeNotifications = new ArrayList<Notifications>();

        Thread p = new Thread() {

            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                JSONObject notificationData = new JSONObject();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    try {
                        //Chercher les notifications de l'utilisateur en envoyant son ID
                        notificationData.append("idUtilisateur", idUtilisateur);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                //Faire la requête
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody postBody = RequestBody.create(JSON, notificationData.toString());
                Request postRequest = new Request.Builder()
                        .url(apiPathGetNotifications)
                        .post(postBody)
                        .build();

                try (Response response = client.newCall(postRequest).execute()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    ResponseBody responseBody = response.body();
                    ObjectMapper mapper = new ObjectMapper();

                    //Récupérer la réponse de l'API
                    JSONObject obj = new JSONObject(response.body().string());
                    JSONArray jsonArray = obj.getJSONArray("notificationsEtTransactions");

                    //Itérer chaque notification pour les instancier en tant qu'objet Notification
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        //Créer un objet JSON pour chaque notification
                        JSONObject notifJSON = jsonArray.getJSONObject(i);

                        //Chercher toutes les données
                        int idNotif = (Integer) notifJSON.get("id_notif");
                        int idTransaction = (Integer) notifJSON.get("idTransaction");
                        int CompteId = (Integer) notifJSON.get("CompteId");
                        String titre = (String) notifJSON.get("titre");
                        String contenu = (String) notifJSON.get("contenu");
                        int lu = (Integer) notifJSON.get("lu");
                        String dateRecu = (String) notifJSON.get("dateRecu");
                        int idCompteBancaireProvenant = (Integer) notifJSON.get("idCompteBancaireProvenant");
                        String dateTransaction = (String) notifJSON.get("dateTransaction");
                        String montant = (String) notifJSON.get("montant");
                        String typeTransaction = (String) notifJSON.get("typeTransaction");
                        int enAttente = (Integer) notifJSON.get("enAttente");

                        Notifications notification;

                        //Si c'est un virement, on crée une notif avec la question et la réponse
                        if(typeTransaction.equals("Virement")) {
                            String question = (String) notifJSON.get("question");
                            String reponse = (String) notifJSON.get("reponse");

                            //Créer la notification et l'ajouter à la liste
                            notification = new Notifications(idNotif, CompteId, idTransaction, titre, contenu, dateRecu, lu, enAttente, question, reponse);
                        }

                        else {
                            //Créer la notification et l'ajouter à la liste
                            notification = new Notifications(idNotif, CompteId, idTransaction, titre, contenu, dateRecu, lu, enAttente);
                        }

                        //Ajouter la notification à la liste
                        listeNotifications.add(notification);

                        //TESTS
                        Log.e("TAG", "ID NOTIF: " + notifJSON.get("id_notif"));
                        Log.e("TAG", "CompteId NOTIF: " + notifJSON.get("CompteId"));
                        Log.e("TAG", "idTransaction NOTIF: " + notifJSON.get("idTransaction"));
                        Log.e("TAG", "titre NOTIF: " + notifJSON.get("titre"));
                        Log.e("TAG", "contenu NOTIF: " + notifJSON.get("contenu"));
                        Log.e("TAG", "dateRecu NOTIF: " + notifJSON.get("dateRecu"));
                        Log.e("TAG", "lu NOTIF: " + notifJSON.get("lu"));
                        Log.e("TAG", "enAttente NOTIF: " + notifJSON.get("enAttente"));
                        Log.e("TAG", "question NOTIF: " + notifJSON.get("question"));
                        Log.e("TAG", "reponse NOTIF: " + notifJSON.get("reponse"));
                    }

                    //Stocker la réponse
                    //recu.setReponse(reponse);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                currentThread().interrupt();
            }
        };

        p.start();
        p.join();

        return listeNotifications;
    }
}
