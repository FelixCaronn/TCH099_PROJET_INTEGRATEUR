package com.example.tch099_projet_integrateur;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tch099_projet_integrateur.info_user.TransactionBancaire;

import java.util.List;

public class historiqueAdapter extends ArrayAdapter<TransactionBancaire> {
    private List<TransactionBancaire> historique;
    private Context contexte;
    private int viewResourceId;
    private Resources ressources;

    public historiqueAdapter(@NonNull Context contexte, int viewResourceId,@NonNull List<TransactionBancaire> historique){
        super(contexte,viewResourceId,historique);
        this.contexte = contexte;
        this.viewResourceId = viewResourceId;
        this.historique = historique;
        this.ressources = contexte.getResources();
    }
    @Override
    public int getCount(){
        return this.historique.size();
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) contexte.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(this.viewResourceId,parent,false);
        }
        final TransactionBancaire transaction = this.historique.get(position);
        if (transaction!=null){
            
        }



        return view;
    }
}
