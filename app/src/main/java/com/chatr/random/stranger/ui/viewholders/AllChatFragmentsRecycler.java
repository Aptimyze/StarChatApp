package com.chatr.random.stranger.ui.viewholders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.chatr.random.stranger.Constants;
import com.chatr.random.stranger.R;
import com.chatr.random.stranger.models.AllChatRecieverInfoModel;
import com.chatr.random.stranger.ui.activities.ChatScreenActivity;
import com.chatr.random.stranger.ui.activities.FullScreenImageActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class AllChatFragmentsRecycler extends RecyclerView.Adapter<AllChatFragmentsRecycler.AllFragmentViewHolder> {

    List<AllChatRecieverInfoModel> allChatRecieverInfo;
    Context c;
    private InterstitialAd mInterstitialAd;


    public AllChatFragmentsRecycler(Context context, List<AllChatRecieverInfoModel> mAllChatRecieverInfo) {
        c = context;
        allChatRecieverInfo = mAllChatRecieverInfo;
        mInterstitialAd = new InterstitialAd(c);
        mInterstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }


    @NonNull
    @Override
    public AllFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.all_chats_card_layout, viewGroup, false);
        return new AllFragmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllFragmentViewHolder allFragmentViewHolder, final int i) {
        final AllChatRecieverInfoModel model = allChatRecieverInfo.get(i);
        allFragmentViewHolder.name.setText(model.recieverName);
        allFragmentViewHolder.last_message.setText(model.lastMsgSentBy + "-" + model.lastMessage );
        long timeStamp = Long.parseLong(model.timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM hh:mm a");
        allFragmentViewHolder.time.setText(simpleDateFormat.format(timeStamp));
        if(model.recieverProfilePic != null && !(model.recieverProfilePic).isEmpty()) {
            Glide.with(c).load(model.recieverProfilePic).into(allFragmentViewHolder.profile_pic);
        }else{
            Glide.with(c).load(R.drawable.placeholder_person).into(allFragmentViewHolder.profile_pic);
        }

        allFragmentViewHolder.profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, FullScreenImageActivity.class);
                i.putExtra(Constants.IMAGE_URL_FOR_FULL_SCREEN,model.recieverProfilePic);
                c.startActivity(i);
            }
        });

        if(!model.isLastMsgRead){
            allFragmentViewHolder.name.setTypeface(null, Typeface.BOLD);
            allFragmentViewHolder.last_message.setTypeface(null, Typeface.BOLD);
            allFragmentViewHolder.time.setTypeface(null, Typeface.BOLD);
            allFragmentViewHolder.not_read_indicator.setVisibility(View.VISIBLE);
        }


        allFragmentViewHolder.full_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent ii = new Intent(c, ChatScreenActivity.class);
                ii.putExtra(Constants.RECIEVER_USERNAME,allChatRecieverInfo.get(i).recieverName);
                ii.putExtra(Constants.RECIEVER_UID,allChatRecieverInfo.get(i).recieverUid);

                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            AdRequest adRequest = new AdRequest.Builder()
                                    .build();
                            mInterstitialAd.loadAd(adRequest);
                            c.startActivity(ii);
                        }
                    });
                }
                else {
                    c.startActivity(ii);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return allChatRecieverInfo.size();
    }

    public class AllFragmentViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_pic,not_read_indicator;
        LinearLayout full_card;
        TextView name, last_message, is_delivered, time;

        public AllFragmentViewHolder(View v) {
            super(v);
            profile_pic = v.findViewById(R.id.profile_image);
            name = v.findViewById(R.id.name);
            last_message = v.findViewById(R.id.last_message);
            is_delivered = v.findViewById(R.id.is_delivered);
            time = v.findViewById(R.id.time);
            full_card = v.findViewById(R.id.full_card);
            not_read_indicator = v.findViewById(R.id.not_read_indicator);
        }

    }

}
