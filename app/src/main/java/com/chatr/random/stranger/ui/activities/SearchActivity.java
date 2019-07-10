package com.chatr.random.stranger.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chatr.random.stranger.R;
import com.chatr.random.stranger.models.SearchedUserModel;
import com.chatr.random.stranger.ui.viewholders.SearchRecycler;
import com.chatr.random.stranger.viewmodels.SearchViewModel;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText search_et;
    SearchViewModel searchViewModel;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        initViews();
        setRecyclerView();
        (getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_abs_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText("Search");


    }

    private void initViews() {
        search_et = findViewById(R.id.search_et);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.spin_kit);

    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }


    public void searchUser(View view) {
        progressBar.setVisibility(View.VISIBLE);
        searchViewModel.returnMatchedUsers(search_et.getText().toString()).observe(this, new Observer<List<SearchedUserModel>>() {
            @Override
            public void onChanged(@Nullable List<SearchedUserModel> searchedUserModels) {
                if(searchedUserModels.size()==0){
                    Toast.makeText(SearchActivity.this, "No User found. Search Again", Toast.LENGTH_SHORT).show();
                }
                recyclerView.setAdapter(new SearchRecycler(getApplicationContext(),searchedUserModels));
                progressBar.setVisibility(View.GONE);
            }
        });


    }
}
