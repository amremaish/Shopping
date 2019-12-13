package com.example.pro.shopping.Activities.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Admin.recyclers.HistoryOffersRecyclerAdapter;
import com.example.pro.shopping.Activities.Authentication.SignInActivity;
import com.example.pro.shopping.Activities.Authentication.forgetPassword;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Offer;
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

import java.util.ArrayList;

public class OffersHistory extends AppCompatActivity {

    private static final String TAG = "OffersHistory";
    private ArrayList<Offer> HistoryOffersList ;
    private android.support.v7.widget.RecyclerView RecyclerView;
    private HistoryOffersRecyclerAdapter HistoryOffersRecyclerAdapter;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private LinearLayout no_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_history);
        RecyclerView = findViewById(R.id.recycler_view);
        mFirebaseAuth = FirebaseAuth.getInstance();
        no_items = findViewById(R.id.no_items);
        no_items.setVisibility(View.GONE);
        LoadFromFirebase();
    }


    private void LoadFromFirebase(){
        HistoryOffersList = new ArrayList<>();
        DatabaseReference UserDatabaseReference = null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.OFFERS);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HistoryOffersList = new ArrayList<>();
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Offer offer = data1.getValue(Offer.class);
                    HistoryOffersList.add(offer);
                }
                LoadAllHistoryOffers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }

    private void LoadAllHistoryOffers() {

        if (HistoryOffersList.isEmpty()){
            no_items.setVisibility(View.VISIBLE);
        }
        HistoryOffersRecyclerAdapter = new HistoryOffersRecyclerAdapter(OffersHistory.this, HistoryOffersList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OffersHistory.this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(HistoryOffersRecyclerAdapter);
    }
}
