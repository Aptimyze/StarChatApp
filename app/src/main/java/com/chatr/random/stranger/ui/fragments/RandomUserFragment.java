package com.chatr.random.stranger.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chatr.random.stranger.Constants;
import com.chatr.random.stranger.R;
import com.chatr.random.stranger.models.NewUserInDbModel;
import com.chatr.random.stranger.models.SearchedUserModel;
import com.chatr.random.stranger.ui.activities.ChatScreenActivity;
import com.chatr.random.stranger.viewmodels.RandomUserViewModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RandomUserFragment extends Fragment {

    public RandomUserFragment() {
    }

    View v;
    RandomUserViewModel randomUserViewModel;
    CardView btn;
    CircleImageView profile_image;
    TextView username_tv, status_tv, start_chat;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_all_group, container, false);
        initView();
        randomUserViewModel = ViewModelProviders.of(getActivity()).get(RandomUserViewModel.class);
        setDetailsIfNotNull();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                observeRandomUserLiveData();
            }
        });

        start_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String randomUid = randomUserViewModel.randomUserUid;
                if (randomUid != null && !randomUid.isEmpty()) {
                    Intent ii = new Intent(getContext(), ChatScreenActivity.class);
                    ii.putExtra(Constants.RECIEVER_USERNAME, randomUserViewModel.randomUserName);
                    ii.putExtra(Constants.RECIEVER_UID, randomUserViewModel.randomUserUid);
                    startActivity(ii);
                } else {
                    Snackbar.make(btn, getString(R.string.random_chat_click_btn), Snackbar.LENGTH_LONG).show();
                }
            }
        });
        return v;


    }

    private void setDetailsIfNotNull() {
        String randomUid = randomUserViewModel.randomUserUid;
        if (randomUid != null && !randomUid.isEmpty()) {
            if(randomUserViewModel.randomUserProfilePic != null && !(randomUserViewModel.randomUserProfilePic).isEmpty()) {
                Glide.with(getContext()).load(randomUserViewModel.randomUserProfilePic).into(profile_image);
            }else{
                Glide.with(getContext()).load(R.drawable.placeholder_person).into(profile_image);
            }
            username_tv.setText(randomUserViewModel.randomUserName);
            status_tv.setText(randomUserViewModel.randomUserStatus);
        }
    }

    private void observeRandomUserLiveData() {
        randomUserViewModel.getRandomUsers().observe(getActivity(), new Observer<NewUserInDbModel>() {
            @Override
            public void onChanged(@Nullable NewUserInDbModel searchedUserModel) {
                if(searchedUserModel.profilePicUrl != null && !(searchedUserModel.profilePicUrl).isEmpty()) {
                    Picasso.with(getContext()).load(searchedUserModel.profilePicUrl).into(profile_image);
                }else{
                    Picasso.with(getContext()).load(R.drawable.placeholder_person).into(profile_image);
                }
                String gender = null;
                if(searchedUserModel.gender!=null && !searchedUserModel.gender.isEmpty()){
                    gender = searchedUserModel.gender;
                }else{
                    gender = "Not Specified";
                }
                username_tv.setText(searchedUserModel.displayName + " (" + gender + ")");
                status_tv.setText(searchedUserModel.status);
                randomUserViewModel.randomUserUid = searchedUserModel.uid;
                randomUserViewModel.randomUserName = searchedUserModel.displayName;
                randomUserViewModel.randomUserProfilePic = searchedUserModel.profilePicUrl;
                randomUserViewModel.randomUserStatus = searchedUserModel.status;
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {
        btn = v.findViewById(R.id.btn);
        profile_image = v.findViewById(R.id.profile_image);
        username_tv = v.findViewById(R.id.username_tv);
        status_tv = v.findViewById(R.id.status_tv);
        start_chat = v.findViewById(R.id.start_chat);
        progressBar = v.findViewById(R.id.spin_kit);
    }


}
