package com.varunsaini.android.bestchatapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.varunsaini.android.bestchatapp.AppPreferences;
import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.ui.activities.SearchActivity;

public class AllChatFragment extends Fragment {

    String recieverUID,senderUID,recieverUserName,senderName;

    public AllChatFragment() {
        // Required empty public constructor
    }

    private View v;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chat, container, false);
        initViews();
        setRecyclerView();
        setHasOptionsMenu(true);

        AppPreferences appPreferences = new AppPreferences(getContext());
        senderUID= appPreferences.readUId();
        senderName = appPreferences.readUName();

        return v;
    }

    private void initViews() {
        recyclerView = v.findViewById(R.id.recyclerView);
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new AllFragmentsRecycler(getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_action_bar,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.addUser){
            Toast.makeText(getContext(), "Add user Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), SearchActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}
