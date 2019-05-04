package com.varunsaini.android.bestchatapp.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.models.SearchedUserModel;
import com.varunsaini.android.bestchatapp.network.Firebase;
import com.varunsaini.android.bestchatapp.ui.fragments.AllFragmentsRecycler;
import com.varunsaini.android.bestchatapp.viewmodels.AllGroupViewModel;
import com.varunsaini.android.bestchatapp.viewmodels.SearchViewModel;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText search_et;
    SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        initViews();
        setRecyclerView();
    }

    private void initViews() {
        search_et = findViewById(R.id.search_et);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }


    public void searchUser(View view) {
        searchViewModel.returnMatchedUsers(search_et.getText().toString()).observe(this, new Observer<List<SearchedUserModel>>() {
            @Override
            public void onChanged(@Nullable List<SearchedUserModel> searchedUserModels) {
                recyclerView.setAdapter(new SearchRecycler(getApplicationContext(),searchedUserModels));

            }
        });


    }
}
