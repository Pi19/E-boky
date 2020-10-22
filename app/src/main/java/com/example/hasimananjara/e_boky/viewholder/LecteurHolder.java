package com.example.hasimananjara.e_boky.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hasimananjara.e_boky.MembreActivity;
import com.example.hasimananjara.e_boky.R;
import com.example.hasimananjara.e_boky.classy.Lecteur;

/**
 * Created by hasimananjara on 06/06/2017.
 */

public class LecteurHolder extends RecyclerView.ViewHolder  {

    private TextView numero;
    private TextView nom;
    private TextView prenom;
    private ImageButton button;
    private Lecteur lecteur ;
    public LecteurHolder (final View itemView)
    {
        super(itemView);

        numero = (TextView) itemView.findViewById(R.id.item_numero);
        nom = (TextView) itemView.findViewById(R.id.item_nom);
        prenom = (TextView) itemView.findViewById(R.id.item_prenom);
        button = (ImageButton) itemView.findViewById(R.id.butlecteur);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //itemView.setBackgroundColor(Color.parseColor("#66e0ff"));
                Intent i = new Intent(v.getContext(), MembreActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("NUM",""+lecteur.getNumLecteur());
                v.getContext().startActivity(i);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),MembreActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("NUM",""+lecteur.getNumLecteur());
                v.getContext().startActivity(i);
            }
        });

    }


    public void bindContact( Lecteur lecteur1)
    {
        lecteur = lecteur1;
        if (lecteur == null)
        {
            //  Log.d(TAG,"Trying to work on a null Contact object ,returning.");
            return;
        }
        //contactTextView.setText(mEmploye.getJid());
        numero.setText(lecteur.getNumLecteur());
        nom.setText(lecteur.getNom());
        prenom.setText(lecteur.getPrenom());
    }
}
