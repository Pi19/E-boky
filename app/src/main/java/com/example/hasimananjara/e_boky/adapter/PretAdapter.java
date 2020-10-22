package com.example.hasimananjara.e_boky.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hasimananjara.e_boky.R;
import com.example.hasimananjara.e_boky.classy.Pret;
import com.example.hasimananjara.e_boky.classy.PretPk;
import com.example.hasimananjara.e_boky.viewholder.LivreHolder;
import com.example.hasimananjara.e_boky.viewholder.PretHolder;

import java.util.List;

/**
 * Created by hasimananjara on 23/06/2017.
 */

public class PretAdapter extends RecyclerView.Adapter<PretHolder> {

    private List<PretPk> pret;

    public PretAdapter(List<PretPk> PretList) {
        pret = PretList;
    }

    @Override
    public PretHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_pret, parent, false);
        return new PretHolder(view);
    }

    @Override
    public void onBindViewHolder(PretHolder holder, int position) {
        PretPk pret1 = pret.get(position);
        holder.bindContact(pret1);
    }


    @Override
    public int getItemCount() {
        return pret.size();
    }
}
