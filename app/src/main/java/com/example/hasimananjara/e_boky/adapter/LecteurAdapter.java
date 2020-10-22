package com.example.hasimananjara.e_boky.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hasimananjara.e_boky.R;
import com.example.hasimananjara.e_boky.classy.Lecteur;
import com.example.hasimananjara.e_boky.viewholder.LecteurHolder;

import java.util.List;

/**
 * Created by hasimananjara on 06/06/2017.
 */

public class LecteurAdapter extends RecyclerView.Adapter<LecteurHolder> {

    private List<Lecteur> lecteur;

    public LecteurAdapter(List<Lecteur> lecteurList) {
        lecteur = lecteurList;
    }

    @Override
    public LecteurHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_lecteur, parent, false);
        return new LecteurHolder(view);
    }

    @Override
    public void onBindViewHolder(LecteurHolder holder, int position) {
        Lecteur lecteur1 = lecteur.get(position);
        holder.bindContact(lecteur1);
    }


    @Override
    public int getItemCount() {
        return lecteur.size();
    }
}