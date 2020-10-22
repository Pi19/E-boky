package com.example.hasimananjara.e_boky.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.hasimananjara.e_boky.PretActivity;
import com.example.hasimananjara.e_boky.R;
import com.example.hasimananjara.e_boky.classy.PretPk;

/**
 * Created by hasimananjara on 23/06/2017.
 */

public class PretHolder extends RecyclerView.ViewHolder {
    private TextView membre;
    private TextView livre;
    private TextView datePret;
    private ImageButton buttonl ;

    public PretPk mpret;

    public PretHolder(View itemView)
    {
        super(itemView);

        livre = (TextView) itemView.findViewById(R.id.item_numeroL);
        datePret = (TextView) itemView.findViewById(R.id.item_Datepret);
        membre = (TextView) itemView.findViewById(R.id.item_numeroM);

        buttonl = (ImageButton) itemView.findViewById(R.id.butPret);

        buttonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //itemView.setBackgroundColor(Color.parseColor("#66e0ff"));
                Intent i = new Intent(v.getContext(),PretActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("NUMliv",""+mpret.getLivre().getNumLivre());
                i.putExtra("NUMlect",""+mpret.getLecteur().getNumLecteur());
                v.getContext().startActivity(i);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),PretActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("NUMliv",""+mpret.getLivre().getNumLivre());
                i.putExtra("NUMlect",""+mpret.getLecteur().getNumLecteur());

                v.getContext().startActivity(i);
            }
        });
    }


    public void bindContact(PretPk contact)
    {
        mpret = contact;
        if (mpret == null)
        {
            //  Log.d(TAG,"Trying to work on a null Contact object ,returning.");
            return;
        }
        //contactTextView.setText(mEmploye.getJid());
         membre.setText(mpret.getLecteur().getNumLecteur());
         livre.setText(mpret.getLivre().getNumLivre());
         datePret.setText(mpret.getDatePret());
    }
}
