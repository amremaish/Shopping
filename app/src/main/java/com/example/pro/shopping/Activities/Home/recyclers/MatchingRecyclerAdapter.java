package com.example.pro.shopping.Activities.Home.recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Admin.RejectAcceptOffer;
import com.example.pro.shopping.Activities.Home.MatchingProfile;
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

import java.util.ArrayList;

public class MatchingRecyclerAdapter extends RecyclerView.Adapter<MatchingRecyclerAdapter.Holder> {
    private static Context mContext;
    private ArrayList<Program> ProgramList ;
    private User curUser ;
    public MatchingRecyclerAdapter(Context mContext , ArrayList<Program> ProgramList) {
        this.mContext = mContext;
        this.ProgramList = ProgramList ;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.matching_offers_recyclerview_item, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {

        holder.profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearcherUser(ProgramList.get(i).getUserCreatorEmail());
            }
        });
        holder.Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActionStatus(Program.ACCEPTED, i);

            }
        });
        holder.Refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionStatus(Program.REJECTED, i);
            }
        });
        holder.DateTime.setText(ProgramList.get(i).getDateOfProgram());
        holder.place.setText(ProgramList.get(i).getLocationOfProgram());

        setScaleAnimation(holder.itemView);
    }

    private  void ActionStatus(final int status , final int i ){
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.PROGRAM);
        UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Program program = data1.getValue(Program.class);
                    String key =  data1.getKey();
                    if (program.getId().equals(ProgramList.get(i).getId())){
                        dataSnapshot.getRef().child(key).child("status").setValue(status);

                        break;
                    }
                }
                String sts ;
                if(status == Program.ACCEPTED){
                    sts = "You have been accepted this program";
                }else {
                    sts = "You have been rejected this program";
                }
                ProgramList.remove(i);
                notifyItemRemoved(i);
                Toast.makeText(mContext,sts,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext,"Failed .",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearcherUser(final String email){
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.USERS).child(FirebaseConstants.USERS);
        UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    User user = data1.getValue(User.class);
                    if (user.getEmail() .equals(email)){
                        curUser  = user ;
                        break;
                    }
                }
                Intent intent  =  new Intent(mContext, MatchingProfile.class);
                intent.putExtra("name" ,curUser.getName() );
                intent.putExtra("BirthDate", curUser.getBirthDate());
                intent.putExtra("Gender" , curUser.getGender());
                intent.putExtra("Email" , curUser.getEmail());
                intent.putExtra("Nature" , curUser.getNature());

                mContext.startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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

        LinearLayout profile_layout ;
        TextView place , DateTime , Refuse , Approve;

        public Holder(View itemView) {
            super(itemView);
            profile_layout  = itemView.findViewById(R.id.profile_layout);
            place  = itemView.findViewById(R.id.place);
            DateTime  = itemView.findViewById(R.id.DateTime);
            Refuse  = itemView.findViewById(R.id.Refuse);
            Approve  = itemView.findViewById(R.id.Approve);
        }
    }
}
