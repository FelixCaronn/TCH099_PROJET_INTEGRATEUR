package com.example.tch099_projet_integrateur.info_user;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;

import com.example.tch099_projet_integrateur.R;
import com.example.tch099_projet_integrateur.enumerations.typeTransaction;
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
            final TextView montantTransaction = (TextView) view.findViewById(R.id.montantTransaction);
            montantTransaction.setText(Double.toString(transaction.getMontant()));
            final TextView descriptionHistorique = (TextView) view.findViewById(R.id.descriptionHistorique);
            ////a changer si on veux plus de detail pour la transaction
            typeTransaction type = transaction.getType();
            switch (type){
                case PAIEMENTFACTURE:
                    descriptionHistorique.setText("Paiement de facture");
                    break;
                case VIREMENT:
                    descriptionHistorique.setText("Virement a une autre personne");
                    break;
                case TRANSFERT:
                    descriptionHistorique.setText("Transfert entre vos comptes");
                    break;
                case DEPOT:
                    descriptionHistorique.setText("Dépot");
                    break;
                default:
                    descriptionHistorique.setText("interet ou wtv");
                    break;
            }
            final TextView date = (TextView) view.findViewById(R.id.date);
            date.setText(transaction.getDateDeTransaction());
        }
        return view;
    }
}
