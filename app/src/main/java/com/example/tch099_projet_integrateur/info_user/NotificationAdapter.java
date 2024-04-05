package com.example.tch099_projet_integrateur.info_user;

import static androidx.core.app.ActivityCompat.recreate;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.pdf.PdfDocument;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tch099_projet_integrateur.ConnexionBD;
import com.example.tch099_projet_integrateur.PagePrincipale;
import com.example.tch099_projet_integrateur.R;

import java.util.List;

/**
 * Adaptateur pour afficher les notifications dans une liste.
 */
public class NotificationAdapter extends ArrayAdapter<Notifications> {

    private List<Notifications> notifications;
    private Context contexte;
    private int viewResourceId;
    private Resources ressources;

    /**
     * Constructeur de la classe NotificationAdapter.
     *
     * @param contexte Le contexte de l'application.
     * @param viewResourceId L'identifiant de la ressource de la vue de l'élément de la liste.
     * @param notifications La liste des notifications à afficher.
     */
    public NotificationAdapter(@NonNull Context contexte, int viewResourceId, @NonNull List<Notifications> notifications){
        super(contexte,viewResourceId,notifications);
        this.contexte = contexte;
        this.viewResourceId = viewResourceId;
        this.notifications = notifications;
        this.ressources = contexte.getResources();
    }

    /**
     * Obtient le nombre total d'éléments dans la liste de notifications.
     *
     * @return Le nombre total d'éléments dans la liste de notifications.
     */
    @Override
    public int getCount(){
        return this.notifications.size();
    }

    /**
     * Obtient une vue qui affiche les données à la position spécifiée dans l'ensemble de données.
     *
     * @param position La position de l'élément dans l'ensemble de données de l'adaptateur.
     * @param convertView La vue réutilisée, si elle existe.
     * @param parent Le parent auquel cette vue sera éventuellement attachée.
     * @return Une vue correspondant aux données à la position spécifiée.
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) contexte.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(this.viewResourceId,parent,false);
        }
        final Notifications noti = this.notifications.get(position);

        if (noti!=null){
            Button supp;
            TextView date, titre, message;
            date = (TextView)view.findViewById(R.id.dateNoti);
            titre = (TextView)view.findViewById(R.id.titre);
            message = (TextView)view.findViewById(R.id.messageNoti);
            supp = (Button) view.findViewById(R.id.supprimer);


            date.setText(noti.getDateRecu());
            titre.setText(noti.getTitre());
            message.setText(noti.getContenu());

            if (noti.getEnAttente() == 1) {
                message.setText(noti.getContenu() + ". Cliquez sur la notification pour accepter/rejeter le virement.");
            }

            // Supprimer de la liste et de la base de données
            supp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Essayer d'effacer la notification
                    try {
                        int idNotifEffacee = ConnexionBD.deleteNotif(PagePrincipale.user.getId(), noti.getId(), "");

                        Log.e("TAG:", "ID NOTIF EFFACÉE: " + idNotifEffacee);
                        Log.e("TAG:", "ID NOTIF: " + noti.getId());


                        // Seulement effacer si la BD retourne l'ID de la notification
                        if (noti.getId() == idNotifEffacee) {
                            // Effacer la notification dynamiquement dans l'adaptateur
                            notifications.remove(position);
                            notifyDataSetChanged();
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return view;
    }
}
