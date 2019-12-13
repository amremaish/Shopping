package com.example.pro.shopping.Activities.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Admin.recyclers.PendingOffersRecyclerAdapter;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Offer;
import com.example.pro.shopping.Models.Program;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Advertisement extends Fragment {
    private android.support.v7.widget.RecyclerView RecyclerView;
    private PendingOffersRecyclerAdapter OffersRecyclerAdapter;
    private ArrayList<Offer> OffersList;
    private static final String TAG = "PendingOffers";
    private DatabaseReference mDatabaseReference;
    private LinearLayout no_items;
    public static TextView counter_notification ;
    private Button layout_notification ;
    private int program_status_counter = 0 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.content_home, container, false);
        RecyclerView = view.findViewById(R.id.recycler_view);
        no_items = view.findViewById(R.id.no_items);
        no_items.setVisibility(View.GONE);
        counter_notification = view.findViewById(R.id.counter_notification);
        layout_notification = view.findViewById(R.id.layout_notification);
        counter_notification.setVisibility(View.GONE);

        LoadOffersFromFirebase();
        LoadStatusProgram();
        notificationClick();
        HistoryClick(view);
        return view;
    }

    private void HistoryClick(View view ) {
        ImageView historyButton = view.findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Advertisement.this.getActivity(), HistoryCreatorProgram.class));
            }
        });

    }

    private void notificationClick() {
        layout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Advertisement.this.getActivity(), ProgramsNotification.class));
            }
        });
    }


    private void LoadOffersFromFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.OFFERS);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                OffersList = new ArrayList<>();
                try {
                    for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                        Offer offer = data1.getValue(Offer.class);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                            Date convertedDateFrom = dateFormat.parse(offer.getFromDateTime());
                            Date convertedDateTo = dateFormat.parse(offer.getToDateTime());
                            Date currentTime = Calendar.getInstance().getTime();
                            long fromDateTime = convertedDateFrom.getTime();
                            long toDateTime = convertedDateTo.getTime();
                            long CurTime = currentTime.getTime();

                            if (CurTime <= toDateTime && CurTime >= fromDateTime
                                    && offer.getStatus() == Offer.ACCEPTED){
                                OffersList.add(offer);
                            }
                    }
                    LoadAllOffers();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });

    }

    private void LoadStatusProgram(){
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.PROGRAM);
        UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                program_status_counter = 0 ;
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Program program = data1.getValue(Program.class);
                    if (program.getUserSearcherEmail().equals(REF.CUR_USER_OBJ.getEmail())
                            && program.getStatus()!= Program.PENDIING
                            && program.getStatusRead() == Program.NOTREAD){
                        program_status_counter++;
                    }
                }
                if (program_status_counter > 0){
                    counter_notification.setVisibility(View.VISIBLE);
                    counter_notification.setText(program_status_counter+"");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Failed .",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadAllOffers() {

        if (OffersList.isEmpty()){
            no_items.setVisibility(View.VISIBLE);
        }

        OffersRecyclerAdapter = new PendingOffersRecyclerAdapter(getContext(), OffersList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(OffersRecyclerAdapter);
    }
}
