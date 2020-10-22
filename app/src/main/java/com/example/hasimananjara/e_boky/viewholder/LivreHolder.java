package com.example.hasimananjara.e_boky.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hasimananjara.e_boky.LivreActivity;
import com.example.hasimananjara.e_boky.R;
import com.example.hasimananjara.e_boky.classy.Livre;

/**
 * Created by hasimananjara on 14/06/2017.
 */

public class LivreHolder extends RecyclerView.ViewHolder {

    private TextView numeroL;
    private TextView designation ;
    private TextView disponibilite;
    private TextView auteur ;
    private Livre livre;
    private ImageButton buttonl ;


    public LivreHolder (final View itemView)
    {
        super(itemView);
        numeroL = (TextView) itemView.findViewById(R.id.item_numeroL);
        designation = (TextView) itemView.findViewById(R.id.item_Designation);
        disponibilite = (TextView) itemView.findViewById(R.id.item_Disponibilite);

        buttonl = (ImageButton) itemView.findViewById(R.id.butlivre);

        buttonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //itemView.setBackgroundColor(Color.parseColor("#66e0ff"));
                Intent i = new Intent(v.getContext(),LivreActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("NUM",""+livre.getNumLivre());
                v.getContext().startActivity(i);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),LivreActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("NUM",""+livre.getNumLivre());
                v.getContext().startActivity(i);
            }
        });


    }
    public void bindContact( Livre livre1)
    {
        livre = livre1;
        if (livre == null)
        {
            //  Log.d(TAG,"Trying to work on a null Contact object ,returning.");
            return;
        }
        //contactTextView.setText(mEmploye.getJid());
        numeroL.setText(livre.getNumLivre());
        designation.setText(livre.getDesignation());
        disponibilite.setText(livre.getDisponible());

    }
}
