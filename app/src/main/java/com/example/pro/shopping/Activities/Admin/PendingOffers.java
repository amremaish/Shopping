package com.example.pro.shopping.Activities.Admin;

import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.pro.shopping.Activities.Admin.recyclers.HistoryOffersRecyclerAdapter;
import com.example.pro.shopping.Activities.Admin.recyclers.PendingOffersRecyclerAdapter;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Offer;
import com.example.pro.shopping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingOffers extends AppCompatActivity {

    private RecyclerView RecyclerView;
    private PendingOffersRecyclerAdapter PendingOffersRecyclerAdapter;
    private static final String TAG = "PendingOffers";
    private DatabaseReference mDatabaseReference;
    private ArrayList<Offer> PendingOffersList;
    private LinearLayout no_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_offers);
        RecyclerView = findViewById(R.id.recycler_view);
        no_items = findViewById(R.id.no_items);
        no_items.setVisibility(View.GONE);
        LoadFromFirebase();
    }

    private void LoadFromFirebase(){

        DatabaseReference UserDatabaseReference = null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.OFFERS);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PendingOffersList = new ArrayList<>();
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Offer offer = data1.getValue(Offer.class);
                    if (offer.getStatus() == Offer.PENDIING)
                    PendingOffersList.add(offer);
                }
                LoadAllPendingOffers();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }

    private void LoadAllPendingOffers() {
        if (PendingOffersList.isEmpty()){
            no_items.setVisibility(View.VISIBLE);
        }
        PendingOffersRecyclerAdapter = new PendingOffersRecyclerAdapter(PendingOffers.this, PendingOffersList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PendingOffers.this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(PendingOffersRecyclerAdapter);
    }
}
