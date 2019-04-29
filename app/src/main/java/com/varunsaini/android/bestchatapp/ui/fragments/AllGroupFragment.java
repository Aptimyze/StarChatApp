package com.varunsaini.android.bestchatapp.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.viewmodels.AllGroupViewModel;

public class AllGroupFragment extends Fragment {

    public AllGroupFragment() {
    }

    View v;
    AllGroupViewModel allGroupViewModel;
    Button btn;
    int i =0 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_all_group, container, false);
        initView();
        allGroupViewModel = ViewModelProviders.of(getActivity()).get(AllGroupViewModel.class);
//        allGroupViewModel.getAllGroupsOfAUser();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add group", Toast.LENGTH_SHORT).show();

                allGroupViewModel.addNewGroup("Title1","This is last msg","21213243"+i);
                i++;
            }
        });
        return v;
    }

    private void initView() {
    btn = v.findViewById(R.id.btn);
    }

}
