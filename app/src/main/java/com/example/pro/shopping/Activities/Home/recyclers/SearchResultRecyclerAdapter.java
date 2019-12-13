package com.example.pro.shopping.Activities.Home.recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Admin.Admin;
import com.example.pro.shopping.Activities.Admin.RejectAcceptOffer;
import com.example.pro.shopping.Activities.Home.InterstedStage;
import com.example.pro.shopping.Activities.Home.Matching;
import com.example.pro.shopping.Activities.Home.MatchingProfile;
import com.example.pro.shopping.Activities.Home.SearchResult;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Offer;
import com.example.pro.shopping.Models.Program;
import com.example.pro.shopping.Models.User;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;


public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.Holder> {
    private static Context mContext;
    private ArrayList<Program> ProgramList ;
    private ArrayList<User> UserNamesList ;
    private  KProgressHUD hud;
    public SearchResultRecyclerAdapter(Context mContext , ArrayList<Program> ProgramList , ArrayList<User> UserNamesList) {
        this.mContext = mContext;
        this.ProgramList = ProgramList ;
        this. UserNamesList = UserNamesList ;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.search_result_recyclerview_item, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {
        // set data
        holder.programTitle.setText(UserNamesList.get(i).getName());

        String[] loc = ProgramList.get(i).getLocationOfProgram().split(" ");
        holder.place.setText(loc[0]+" - "+loc[1]);
        String[] date =  ProgramList.get(i).getDateOfProgram().split(" ");
        holder.DateTime.setText(date[0]+" - "+date[1]);
        String riding ="" ;
        if (!ProgramList.get(i).isRiding()){
            riding="Not ";
        }
        riding = "Available Riding";
        holder.share_riding.setText(riding);

        holder.ViewProfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(mContext , MatchingProfile.class);
                intent.putExtra("name" , UserNamesList.get(i).getName());
                intent.putExtra("BirthDate" , UserNamesList.get(i).getBirthDate());
                intent.putExtra("Nature" , UserNamesList.get(i).getNature());
                intent.putExtra("ProgramsNumber" , UserNamesList.get(i).getProgramsNumber());
                intent.putExtra("Gender" , UserNamesList.get(i).getGender());
                intent.putExtra("Email" , UserNamesList.get(i).getEmail());
                mContext.startActivity(intent);
            }
        });
        hud = KProgressHUD.create(mContext).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        holder.interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hud.show();
                SendRequest(i);

            }
        });
        setScaleAnimation(holder.itemView);
    }

    private void SendRequest(final int i ) {

        Intent intent =  new Intent(mContext , InterstedStage.class);
        intent.putExtra("name" ,UserNamesList.get(i).getName());
        mContext.startActivity(intent);

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.PROGRAM);
        UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Program program = data1.getValue(Program.class);
                    String key =  data1.getKey();
                    if (program.getId().equals(ProgramList.get(i).getId()) ){
                        dataSnapshot.getRef().child(key).child("userSearcherEmail").setValue(REF.CUR_USER_OBJ.getEmail());
                        break;
                    }
                }

                hud.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hud.dismiss();
                Toast.makeText(mContext,"Failed .",Toast.LENGTH_SHORT).show();
            }
        });

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
        return ProgramList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        Button ViewProfBtn , interested ;
        TextView programTitle,place,DateTime,share_riding ;
        public Holder(View itemView) {
            super(itemView);
            ViewProfBtn = itemView.findViewById(R.id.view_profile_btn);
            interested = itemView.findViewById(R.id.interested);
            programTitle = itemView.findViewById(R.id.programTitle);
            place = itemView.findViewById(R.id.place);
            DateTime = itemView.findViewById(R.id.DateTime);
            share_riding = itemView.findViewById(R.id.share_riding);
        }


    }
}
