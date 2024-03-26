package com.example.tch099_projet_integrateur.info_user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tch099_projet_integrateur.R;

import java.util.List;

public class CompteAdapter extends ArrayAdapter<CompteBancaire> {
    private List<CompteBancaire> comptes;
    private Context contexte;
    private int viewResourceId;

    public CompteAdapter(@NonNull Context context, int resource, @NonNull List<CompteBancaire> comptes)
    {
        super(context, resource, comptes);

        this.contexte = context;
        this.viewResourceId = resource;
        this.comptes = comptes;
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) contexte.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(this.viewResourceId, parent, false);
        }

        final CompteBancaire compte = this.comptes.get(position);

        if (compte != null) {
            final TextView tv_TypeCompte = (TextView) view.findViewById(R.id.item_TypeCompte);
            final TextView tv_NumCompte = (TextView) view.findViewById(R.id.item_numCompte);
            final TextView tv_MontantCompte = (TextView) view.findViewById(R.id.item_MontantCompte);

            tv_TypeCompte.setText(compte.getTypeCompte().name());
            tv_NumCompte.setText(String.format("%d",compte.getNumCompte()));
            tv_MontantCompte.setText(String.format("%.2f", compte.getSolde()));
        }

        return view;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }
}
