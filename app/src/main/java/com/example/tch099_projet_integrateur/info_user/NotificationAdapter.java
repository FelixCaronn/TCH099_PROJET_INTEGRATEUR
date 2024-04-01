package com.example.tch099_projet_integrateur.info_user;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tch099_projet_integrateur.R;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notifications> {

    private List<Notifications> notifications;
    private Context contexte;
    private int viewResourceId;
    private Resources ressources;

    public NotificationAdapter(@NonNull Context contexte, int viewResourceId, @NonNull List<Notifications> notifications){
        super(contexte,viewResourceId,notifications);
        this.contexte = contexte;
        this.viewResourceId = viewResourceId;
        this.notifications = notifications;
        this.ressources = contexte.getResources();
    }
    @Override
    public int getCount(){
        return this.notifications.size();
    }

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

            //delete de la liste et de la base de donnes
            supp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //quoi faire si c'est un virement et qu'il faut afficher la question et demander la reponse? un popup?
        }
        return view;
    }
}
