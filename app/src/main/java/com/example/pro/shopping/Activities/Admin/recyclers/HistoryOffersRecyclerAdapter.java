package com.example.pro.shopping.Activities.Admin.recyclers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pro.shopping.Activities.Admin.offer_details;
import com.example.pro.shopping.Models.Offer;
import com.example.pro.shopping.R;

import java.util.ArrayList;

public class HistoryOffersRecyclerAdapter extends RecyclerView.Adapter<HistoryOffersRecyclerAdapter.Holder> {
    private static Context mContext;
    private ArrayList<Offer> HistoryOffersList ;
    private final static int FADE_DURATION = 500;
    public HistoryOffersRecyclerAdapter(Context mContext , ArrayList<Offer> HistoryOffersList) {
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
        holder.from_date_txt.setText(HistoryOffersList.get(i).getFromDateTime());
        holder.to_date_txt.setText(HistoryOffersList.get(i).getToDateTime());
        if (HistoryOffersList.get(i).getStatus() == Offer.ACCEPTED){
            holder.status.setText("Accepted");
            holder.status.setTextColor(Color.parseColor("#3CB371"));
        }else if (HistoryOffersList.get(i).getStatus() == Offer.PENDIING){
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.parseColor("#FBAC39"));
        }else if (HistoryOffersList.get(i).getStatus() == Offer.REJECTED){
            holder.status.setText("Rejected");
            holder.status.setTextColor(Color.parseColor("#FB3939"));
        }
        holder.company_name .setText(HistoryOffersList.get(i).getCompanyName());
        holder.offer_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(mContext , offer_details.class);
                intent.putExtra("ComapnyName", HistoryOffersList.get(i).getCompanyName());
                intent.putExtra("Desc", HistoryOffersList.get(i).getDesc());
                intent.putExtra("FromDateTime", HistoryOffersList.get(i).getFromDateTime());
                intent.putExtra("ToDateTime", HistoryOffersList.get(i).getToDateTime());
                intent.putExtra("imagePath", HistoryOffersList.get(i).getImagePath());

                mContext.startActivity(intent);
            }
        });
        setScaleAnimation(holder.itemView);
    }


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }



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
        TextView from_date_txt , to_date_txt , status , company_name;
        LinearLayout offer_details ;
        public Holder(View itemView) {
            super(itemView);
            from_date_txt = itemView.findViewById(R.id.from_date_txt);
            to_date_txt = itemView.findViewById(R.id.to_date_txt);
            status = itemView.findViewById(R.id.status);
            offer_details = itemView.findViewById(R.id.offer_details);
            company_name = itemView.findViewById(R.id.company_name);
        }


    }
}
