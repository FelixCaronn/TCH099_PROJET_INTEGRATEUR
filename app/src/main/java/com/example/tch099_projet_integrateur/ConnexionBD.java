package com.example.tch099_projet_integrateur;
import com.example.tch099_projet_integrateur.enumerations.typeCompte;
import com.example.tch099_projet_integrateur.info_user.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;

import android.util.Log;
import android.widget.ListView;

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


    private static final String apiPathVerifLogin = "http://34.127.103.229/TCH099_FishFric/Site_web/Connexion/API/apiConnexion.php";
    private static final String apiPathCreationCompte = "http://34.127.103.229/TCH099_FishFric/Site_web/Creer_un_compte/API/apiCreerCompte.php";

    private static final String apiPathListeComptes = "http://34.127.103.229/TCH099_FishFric/Site_web/Liste_compte/API/afficherComptes.php";


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
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    ResponseBody responseBody = response.body();
                    ObjectMapper mapper = new ObjectMapper();


                    JsonNode json = mapper.readTree(responseBody.string());
                    String reponse = json.get("reponse").asText();
                    String codeRes = json.get("code").asText();
                    String userNom = json.get("nom").asText();
                    int userId = json.get("id").asInt();
                    int code = Integer.parseInt(codeRes);
                    verifLog.setCode(code);
                    verifLog.setReponse(reponse);
                    verifLog.setNom(userNom);
                    verifLog.setId(userId);


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
                        Log.e("TAG", "Num:"+tmp1.getNumCompte()
                        +"\n"+"Solde :"+tmp1.getSolde() + "\n" + "Type: " + tmp1.getTypeCompte());


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

}
