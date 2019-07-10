package com.chatr.random.stranger.ui.fragments;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.android.gms.ads.InterstitialAd;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.chatr.random.stranger.AppPreferences;
import com.chatr.random.stranger.R;
import com.chatr.random.stranger.models.AllChatRecieverInfoModel;
import com.chatr.random.stranger.ui.activities.SearchActivity;
import com.chatr.random.stranger.ui.viewholders.AllChatFragmentsRecycler;
import com.chatr.random.stranger.viewmodels.AllChatViewModel;

import java.util.List;

public class AllChatFragment extends Fragment {

    String recieverUID,senderUID,recieverUserName,senderName;

    private View v;
    RecyclerView recyclerView;
    AllChatViewModel allChatViewModel;
    private InterstitialAd mInterstitialAd;
    private Context mContext;


    public AllChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chat, container, false);
        initViews();
        setRecyclerView();
        setHasOptionsMenu(true);
        allChatViewModel = ViewModelProviders.of(getActivity()).get(AllChatViewModel.class);
        AppPreferences appPreferences = new AppPreferences(getContext());
        senderUID= appPreferences.readUId();
        senderName = appPreferences.readUName();
        allChatViewModel.mContext = getContext();

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        allChatViewModel.getAllChatListOfAParticularUser(senderUID).observe(getActivity(), new Observer<List<AllChatRecieverInfoModel>>() {
            @Override
            public void onChanged(@Nullable List<AllChatRecieverInfoModel> allChatRecieverInfoModels) {
                recyclerView.setAdapter(new AllChatFragmentsRecycler(mContext ,allChatRecieverInfoModels));
            }
        });



        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext  = context;
    }

    private void initViews() {
        recyclerView = v.findViewById(R.id.recyclerView);
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
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
            Toast.makeText(getContext(), "Search Users", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), SearchActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
