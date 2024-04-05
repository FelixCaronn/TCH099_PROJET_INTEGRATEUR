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

/**
 * Classe adaptateur pour la liste des comptes de l'utilisateur.
 * Cet adaptateur lie les données de la liste des comptes bancaires à une vue ListView.
 */
public class CompteAdapter extends ArrayAdapter<CompteBancaire> {
    private List<CompteBancaire> comptes;
    private Context contexte;
    private int viewResourceId;

    /**
     * Constructeur de la classe CompteAdapter.
     *
     * @param context Le contexte de l'application.
     * @param resource L'identifiant de la ressource de la vue de l'élément de la liste.
     * @param comptes La liste des comptes bancaires à afficher.
     */
    public CompteAdapter(@NonNull Context context, int resource, @NonNull List<CompteBancaire> comptes)
    {
        super(context, resource, comptes);
        this.contexte = context;
        this.viewResourceId = resource;
        this.comptes = comptes;
    }

    /**
     * Obtient une vue qui affiche les données à la position spécifiée dans l'ensemble de données.
     *
     * @param position La position de l'élément dans l'ensemble de données de l'adaptateur.
     * @param view La vue réutilisée, si elle existe.
     * @param parent Le parent auquel cette vue sera éventuellement attachée.
     * @return Une vue correspondant aux données à la position spécifiée.
     */
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

    /**
     * Obtient le nombre total d'éléments dans le ListView.
     *
     * @return Le nombre total d'éléments dans le ListView.
     */
    @Override
    public int getCount() {
        return super.getCount();
    }
}
