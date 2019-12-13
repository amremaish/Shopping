package com.example.pro.shopping.Activities.Admin.recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pro.shopping.Activities.Admin.RejectAcceptOffer;
import com.example.pro.shopping.Activities.Admin.offer_details;
import com.example.pro.shopping.REF;
import com.example.pro.shopping.Models.Offer;
import com.example.pro.shopping.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PendingOffersRecyclerAdapter extends RecyclerView.Adapter<PendingOffersRecyclerAdapter.Holder> {
    private static Context mContext;
    private ArrayList<Offer> PendingOffersList ;
    public PendingOffersRecyclerAdapter(Context mContext ,  ArrayList<Offer> PendingOffersList) {
        this.mContext = mContext;
        this.PendingOffersList = PendingOffersList ;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.offers_recyclerview_item, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {

        Picasso.get().load(PendingOffersList.get(i).getImagePath()).into(holder.Adv_image);
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (REF.CUR_USER == REF.ADMIN) {
                    Intent intent = new Intent(mContext, RejectAcceptOffer.class);
                    intent.putExtra("ComapnyName", PendingOffersList.get(i).getCompanyName());
                    intent.putExtra("Desc", PendingOffersList.get(i).getDesc());
                    intent.putExtra("FromDateTime", PendingOffersList.get(i).getFromDateTime());
                    intent.putExtra("ToDateTime", PendingOffersList.get(i).getToDateTime());
                    intent.putExtra("imagePath", PendingOffersList.get(i).getImagePath());
                    intent.putExtra("email", PendingOffersList.get(i).getCompany_email());
                   mContext.startActivity(intent);
                }
            }
        });
        setScaleAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    private final static int FADE_DURATION = 500;
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


    @Override
    public int getItemCount() {
        return PendingOffersList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LinearLayout request ;
        ImageView Adv_image ;
        public Holder(View itemView) {
            super(itemView);
            request  = itemView.findViewById(R.id.request);
            Adv_image  = itemView.findViewById(R.id.Adv_image);
        }


    }
}
