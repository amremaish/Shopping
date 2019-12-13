package com.example.pro.shopping.Activities.Home.recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import com.example.pro.shopping.Models.Offer;
import com.example.pro.shopping.R;
import java.util.ArrayList;


public class OffersRecyclerAdapter extends RecyclerView.Adapter<OffersRecyclerAdapter.Holder> {
    private static Context mContext;
    private ArrayList<Offer> HistoryOffersList ;
    public OffersRecyclerAdapter(Context mContext , ArrayList<Offer> HistoryOffersList) {
        this.mContext = mContext;
        this.HistoryOffersList = HistoryOffersList ;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.history_offers_recyclerview_item, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {
        setScaleAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private final static int FADE_DURATION = 1000;
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return HistoryOffersList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }


    }
}
