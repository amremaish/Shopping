package com.example.pro.shopping.Activities.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Program;
import com.example.pro.shopping.Models.Rate;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchingProfile extends AppCompatActivity {

    private static final String TAG ="MatchingProfile" ;
    private TextView email ,number_of_program , name ,age ,gender , nature_of_person;
    private DatabaseReference mDatabaseReference;
    private float rating_counter ,pre_rating;
    private RatingBar RatingBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_profile);
        InflateUi();
        LoadDataIntent();
        setRating();
    }

    private void InflateUi(){
        email = findViewById(R.id.email);
        number_of_program = findViewById(R.id.number_of_program);
        age = findViewById(R.id.age);
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        nature_of_person = findViewById(R.id.nature_of_person);
        RatingBar = findViewById(R.id.RatingBar);
        GetAllProgram();
    }

    private void LoadDataIntent() {

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        age.setText(intent.getStringExtra("BirthDate"));
        gender.setText(intent.getStringExtra("Gender"));
        email.setText(intent.getStringExtra("Email"));
        nature_of_person.setText(intent.getStringExtra("Nature"));
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
                    if (Rate.getRatedEmail().equals(getIntent().getStringExtra("Email"))){
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
                    if (Program.getUserCreatorEmail().equals(email.getText().toString()) ||
                            Program.getUserSearcherEmail().equals((email.getText().toString()))){
                        counter++;
                    }
                }
                number_of_program.setText(counter+"");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }


}
