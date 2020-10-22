package com.example.hasimananjara.e_boky.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hasimananjara.e_boky.R;
import com.example.hasimananjara.e_boky.classy.Livre;
import com.example.hasimananjara.e_boky.viewholder.LivreHolder;

import java.util.List;

/**
 * Created by hasimananjara on 14/06/2017.
 */

public class LivreAdapter extends RecyclerView.Adapter<LivreHolder> {


    private List<Livre> livre;

    public LivreAdapter(List<Livre> livreList) {
        livre = livreList;
    }

    @Override
    public LivreHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_livre, parent, false);
        return new LivreHolder(view);
    }

    @Override
    public void onBindViewHolder(LivreHolder holder, int position) {
        Livre livre1 = livre.get(position);
        holder.bindContact(livre1);
    }


    @Override
    public int getItemCount() {
        return livre.size();
    }
}
