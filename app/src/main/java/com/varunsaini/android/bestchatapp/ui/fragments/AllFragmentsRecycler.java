package com.varunsaini.android.bestchatapp.ui.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.varunsaini.android.bestchatapp.R;

public class AllFragmentsRecycler extends RecyclerView.Adapter<AllFragmentsRecycler.AllFragmentViewHolder> {
    public AllFragmentsRecycler(Context context) {
    }


    @NonNull
    @Override
    public AllFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AllFragmentViewHolder allFragmentViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AllFragmentViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public AllFragmentViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

}
