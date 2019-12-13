package com.example.pro.shopping.Activities.Home.recyclers;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pro.shopping.Models.Program;
import com.example.pro.shopping.Models.Rate;
import com.example.pro.shopping.R;

import java.util.ArrayList;


public class HistoryProgramsRecyclerAdapter extends RecyclerView.Adapter<HistoryProgramsRecyclerAdapter.Holder>  {
    private static final int TAG = 1;
    private static AppCompatActivity mContext;
    private ArrayList<Program> HistoryProgramList;

    private final static int FADE_DURATION = 500;
    private  Rate rate ;
    private boolean once ;
    public HistoryProgramsRecyclerAdapter(AppCompatActivity mContext , ArrayList<Program> HistoryOffersList) {
        this.mContext = mContext;
        this.HistoryProgramList = HistoryOffersList ;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.history_programs_recyclerview_item, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {
        holder.place.setText(HistoryProgramList.get(i).getLocationOfProgram());
        holder.DateTime.setText(HistoryProgramList.get(i).getDateOfProgram());
        if (HistoryProgramList.get(i).getStatus() == Program.ACCEPTED){
            holder.status.setText("Accepted");
            holder.status.setTextColor(Color.parseColor("#3CB371"));
        }else if (HistoryProgramList.get(i).getStatus() == Program.PENDIING){
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.parseColor("#FBAC39"));
        }else if (HistoryProgramList.get(i).getStatus() == Program.REJECTED){
            holder.status.setText("Rejected");
            holder.status.setTextColor(Color.parseColor("#FB3939"));
        }

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
        return HistoryProgramList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView place,DateTime, status;
        LinearLayout profile_layout ;
        public Holder(View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.place);
            DateTime = itemView.findViewById(R.id.DateTime);
            status = itemView.findViewById(R.id.status);
        }


    }
}
