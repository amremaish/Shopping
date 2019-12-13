package com.example.pro.shopping.Activities.Home;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Admin.Admin;
import com.example.pro.shopping.Activities.Authentication.SignInActivity;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Offer;
import com.example.pro.shopping.Models.Program;
import com.example.pro.shopping.Models.Rate;
import com.example.pro.shopping.Models.User;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

public class profile extends Fragment {

    private static final String TAG = "profile";
    private DatabaseReference mDatabaseReference;
    private TextView numberOfProgram;
    private User CurUser ;
    private KProgressHUD hud ;
    private RatingBar RatingBar ;
    private float rating_counter , pre_rating = -1 ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        TextView email = view.findViewById(R.id.email);
        TextView age =  view.findViewById(R.id.age);
        TextView name =  view.findViewById(R.id.name);
        TextView gender =  view.findViewById(R.id.gender);
         RatingBar =  view.findViewById(R.id.RatingBar);
        TextView natureOfPerson =  view.findViewById(R.id.nature_of_person);
        CurUser = (User) REF.CUR_USER_OBJ;
        email.setText(CurUser.getEmail());
        age.setText(CurUser.getBirthDate());
        name.setText(CurUser.getName());
        gender.setText(CurUser.getGender());
        natureOfPerson.setText(CurUser.getNature());
        numberOfProgram =  view.findViewById(R.id.number_of_program);
        hud = KProgressHUD.create(getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        GetAllProgram();
        setRating();
        return view ;
    }


    private void setRating(){

        DatabaseReference UserDatabaseReference = null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.RATING);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Rate Rate = data1.getValue(Rate.class);
                    if (Rate.getRatedEmail().equals(CurUser.getEmail())){
                        if (pre_rating != -1 )
                            rating_counter+=(((Rate.getRating() - pre_rating) /2.0)+ pre_rating);
                        else{
                            rating_counter  = Rate.getRating() ;
                        }
                    }
                }
                RatingBar.setRating(rating_counter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }

    private void GetAllProgram() {
        DatabaseReference UserDatabaseReference = null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.PROGRAM);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0 ;
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Program Program = data1.getValue(Program.class);
                    if (Program.getUserCreatorEmail().equals(CurUser.getEmail()) ||
                            Program.getUserSearcherEmail().equals(CurUser.getEmail())){
                        counter++;
                    }

                }
                numberOfProgram.setText(counter+"");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }


}
