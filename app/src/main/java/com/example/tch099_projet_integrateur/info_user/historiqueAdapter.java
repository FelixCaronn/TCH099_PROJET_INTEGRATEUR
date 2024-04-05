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

/**
 * Adaptateur pour afficher l'historique des transactions bancaires dans une liste.
 */
public class historiqueAdapter extends ArrayAdapter<TransactionBancaire> {
    private List<TransactionBancaire> historique;
    private Context contexte;
    private int viewResourceId;
    private Resources ressources;

    /**
     * Constructeur de la classe historiqueAdapter.
     *
     * @param contexte Le contexte de l'application.
     * @param viewResourceId L'identifiant de la ressource de la vue de l'élément de la liste.
     * @param historique La liste des transactions bancaires à afficher dans l'historique.
     */
    public historiqueAdapter(@NonNull Context contexte, int viewResourceId, @NonNull List<TransactionBancaire> historique) {
        super(contexte, viewResourceId, historique);
        this.contexte = contexte;
        this.viewResourceId = viewResourceId;
        this.historique = historique;
        this.ressources = contexte.getResources();
    }

    /**
     * @return Le nombre total d'éléments dans l'historique des transactions.
     */
    @Override
    public int getCount() {
        return this.historique.size();
    }

    /**
     * Obtient une vue qui affiche les données à la position spécifiée dans l'ensemble de données.
     *
     * @param position La position de l'élément dans l'ensemble de données de l'adaptateur.
     * @param convertView La vue réutilisée, si elle existe.
     * @param parent Le parent auquel cette vue sera éventuellement attachée.
     * @return Une vue correspondant aux données à la position spécifiée.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) contexte.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(this.viewResourceId, parent, false);
        }

        final TransactionBancaire transaction = this.historique.get(position);

        if (transaction != null) {
            final TextView montantTransaction = (TextView) view.findViewById(R.id.montantTransaction);
            montantTransaction.setText(String.format("%.2f", transaction.getMontant()));

            // Mettre la couleur selon le virement sortant ou entrant
            if (transaction.getMontant() < 0)
                montantTransaction.setTextColor(Color.parseColor("#ef233c"));
            else
                montantTransaction.setTextColor(Color.parseColor("#55a630"));

            final TextView descriptionHistorique = (TextView) view.findViewById(R.id.descriptionHistorique);
            typeTransaction type = transaction.getType();

            switch (type) {
                case PAIEMENTFACTURE:
                    descriptionHistorique.setText("Paiement de facture / " + transaction.getNomEtablissement());
                    break;
                case VIREMENT:
                    // Si le virement vient de nous, on met le nom du destinataire
                    if (transaction.getMontant() < 0)
                        descriptionHistorique.setText("Virement / " + transaction.getNomEtablissement());
                        // Sinon, on met le courriel de la personne qui nous a envoyé le virement
                    else
                        descriptionHistorique.setText("Virement / " + transaction.getProvenance());
                    break;

                case VIREMENT_ACCEPTE:
                    // Si le virement vient de nous, on met le nom du destinataire
                    if (transaction.getMontant() < 0)
                        descriptionHistorique.setText("Virement accepté/ " + transaction.getNomEtablissement());
                        // Sinon, on met le courriel de la personne qui nous a envoyé le virement
                    else
                        descriptionHistorique.setText("Virement accepté/ " + transaction.getProvenance());
                    break;

                case TRANSFERT:
                    // Si le transfert vient du compte dans lequel on est maintenant
                    if (transaction.getMontant() < 0)
                        descriptionHistorique.setText("Transfert entre comptes / " + transaction.getIdCompteRecevant());
                        // Sinon, on met le courriel de la personne qui nous a envoyé le virement
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
