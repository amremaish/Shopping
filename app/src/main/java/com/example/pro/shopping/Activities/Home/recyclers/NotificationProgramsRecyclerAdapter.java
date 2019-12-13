package com.example.pro.shopping.Activities.Home.recyclers;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Home.MatchingProfile;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Program;
import com.example.pro.shopping.Models.Rate;
import com.example.pro.shopping.Models.User;

import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class NotificationProgramsRecyclerAdapter extends RecyclerView.Adapter<NotificationProgramsRecyclerAdapter.Holder>  {
    private static final int TAG = 1;
    private static AppCompatActivity mContext;
    private ArrayList<Program> NotificationProgramList;
    private ArrayList<User> UserList;
    private final static int FADE_DURATION = 500;
    private  Rate rate ;
    private boolean once ;
    public NotificationProgramsRecyclerAdapter(AppCompatActivity mContext , ArrayList<Program> HistoryOffersList , ArrayList<User> UserList) {
        this.mContext = mContext;
        this.NotificationProgramList = HistoryOffersList ;
        this.UserList = UserList ;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.notification_programs_recyclerview_item, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {
        holder.user_name.setText(UserList.get(i).getName());
        holder.email.setText(UserList.get(i).getEmail());
        holder.place.setText(NotificationProgramList.get(i).getLocationOfProgram());
        holder.DateTime.setText(NotificationProgramList.get(i).getDateOfProgram());
        if (NotificationProgramList.get(i).getStatus() == Program.ACCEPTED){
            holder.status.setText("Accepted");
            holder.status.setTextColor(Color.parseColor("#3CB371"));
            holder.RateButton.setVisibility(View.VISIBLE);
        }else if (NotificationProgramList.get(i).getStatus() == Program.PENDIING){
            holder.status.setText("Pending");
            holder.status.setTextColor(Color.parseColor("#FBAC39"));
        }else if (NotificationProgramList.get(i).getStatus() == Program.REJECTED){
            holder.status.setText("Rejected");
            holder.status.setTextColor(Color.parseColor("#FB3939"));
        }

        holder.profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(mContext , MatchingProfile.class);
                intent.putExtra("name" , UserList.get(i).getName());
                intent.putExtra("BirthDate" , UserList.get(i).getBirthDate());
                intent.putExtra("Nature" , UserList.get(i).getNature());
                intent.putExtra("ProgramsNumber" , UserList.get(i).getProgramsNumber());
                intent.putExtra("Gender" , UserList.get(i).getGender());
                intent.putExtra("Email" , UserList.get(i).getEmail());
                mContext.startActivity(intent);
            }
        });
        setScaleAnimation(holder.itemView);

        holder.RateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.rate_dialog);
                Button RateButton = dialog.findViewById(R.id.rateButton);
               final RatingBar RatingBar = dialog.findViewById(R.id.RatingBar);
                dialog.setTitle("Rate Your friend");

                RateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        once = false ;
                        IsUserAlreadyRated(UserList.get(i).getEmail() ,RatingBar , dialog );
                    }
                });

                dialog.show();
            }
        });
    }

    private void IsUserAlreadyRated(final String email , final RatingBar RatingBar , final Dialog dialog ){
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.RATING);
            UserDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean rated = false ;
                    for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                        Rate Rate = data1.getValue(Rate.class);
                        if (Rate.getRateFromEmail().equals(REF.CUR_USER_OBJ.getEmail())) {
                            rated = true ;
                        }
                    }
                    if (!rated) {
                        once = true ;
                        SendRate(email, RatingBar, dialog);
                    }else if (!once) {
                        Toast.makeText(mContext, "You already rated this user.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(String.valueOf(TAG), "onCancelled: ");
                }
            });

    }

    private void SendRate(String email ,  RatingBar RatingBar , final Dialog dialog ){
        rate = new Rate();
        rate.setRatedEmail(email);
        rate.setRateFromEmail(REF.CUR_USER_OBJ.getEmail());
        rate.setRating(RatingBar.getRating());
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.RATING);
        UserDatabaseReference.push().setValue(rate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();

                    Log.e("Success", "Successfully:Success", task.getException());
                    Toast.makeText(mContext, "Successfully Rated ", Toast.LENGTH_SHORT).show();
                }else {
                    dialog.dismiss();
                    Log.e("failure", "createProfile:failure", task.getException());
                }
            }
        });

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
        return NotificationProgramList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView user_name ,email ,place,DateTime, status , RateButton;
        LinearLayout profile_layout ;
        public Holder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.email);
            place = itemView.findViewById(R.id.place);
            DateTime = itemView.findViewById(R.id.DateTime);
            status = itemView.findViewById(R.id.status);
            profile_layout = itemView.findViewById(R.id.profile_layout);
            RateButton = itemView.findViewById(R.id.RateButton);
        }


    }
}
