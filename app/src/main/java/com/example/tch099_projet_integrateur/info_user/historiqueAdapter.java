package com.example.tch099_projet_integrateur.info_user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;
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
            montantTransaction.setText(String.format("%.2f", transaction.getMontant()));

            //Mettre la couleur selon le virement sortant ou entrant
            if(transaction.getMontant() < 0)
                montantTransaction.setTextColor(Color.parseColor("#ef233c"));
            else
                montantTransaction.setTextColor(Color.parseColor("#55a630"));

            final TextView descriptionHistorique = (TextView) view.findViewById(R.id.descriptionHistorique);
            ////a changer si on veux plus de detail pour la transaction
            typeTransaction type = transaction.getType();

            switch (type){
                case PAIEMENTFACTURE:
                    descriptionHistorique.setText("Paiement de facture / " + transaction.getNomEtablissement());
                    break;
                case VIREMENT:
                    //Si le virement vient de nous, on met le nom du destinataire
                    if(transaction.getMontant() < 0)
                        descriptionHistorique.setText("Virement / " + transaction.getNomEtablissement());

                    //Sinon, on met le courriel de la personne qui nous a envoyé le virement
                    else
                        descriptionHistorique.setText("Virement / " + transaction.getProvenance());

                    break;
                case TRANSFERT:
                    //Si le transfert vient du compte dans lequel on est maintenant
                    if(transaction.getMontant() < 0)
                        descriptionHistorique.setText("Transfert entre comptes / " + transaction.getIdCompteRecevant());

                        //Sinon, on met le courriel de la personne qui nous a envoyé le virement
                    else
                        descriptionHistorique.setText("Transfert entre comptes / " + transaction.getCompteProvenance());

                    break;
                case DEPOT:
                    descriptionHistorique.setText("Dépôt mobile");
                    break;
                case VIREMENT_REFUSE:
                    descriptionHistorique.setText("Virement refusé / " + transaction.getNomEtablissement());
                    break;
                default:
                    descriptionHistorique.setText("Autre type");
                    break;
            }
            final TextView date = (TextView) view.findViewById(R.id.date);
            date.setText(transaction.getDateDeTransaction());
        }
        return view;
    }
}
