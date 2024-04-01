package com.example.tch099_projet_integrateur.info_user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tch099_projet_integrateur.R;
import com.example.tch099_projet_integrateur.enumerations.typeTransaction;

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
        //     = view.findViewById(R.id.);
        }
        return view;
    }
}
