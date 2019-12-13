package com.example.pro.shopping.Activities.Home;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Home.recyclers.MatchingRecyclerAdapter;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Program;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Matching extends Fragment {
    private android.support.v7.widget.RecyclerView RecyclerView;
    private MatchingRecyclerAdapter MatchingRecyclerAdapter;
    private ArrayList<Program> ProgramList;
    private LinearLayout no_items;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.activity_matching, container, false);
        RecyclerView = view.findViewById(R.id.recycler_view);
        no_items = view.findViewById(R.id.no_items);
        no_items.setVisibility(View.GONE);
        LoadAllRequests();
        return view;
    }

    private void LoadAllRequests() {
            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.PROGRAM);
            UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ProgramList = new ArrayList<>();
                    for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                        Program program = data1.getValue(Program.class);
                        if (program.getUserCreatorEmail().equals(REF.CUR_USER_OBJ.getEmail())
                                && !program.getUserSearcherEmail().equals(Program.NULL)
                                && program.getStatus() == Program.PENDIING){
                            ProgramList.add(program);
                        }
                    }
                    LoadIntoRecycler();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(),"Failed .",Toast.LENGTH_SHORT).show();
                }
            });

        }


    private void LoadIntoRecycler() {
        if (ProgramList.isEmpty()){
            no_items.setVisibility(View.VISIBLE);
        }
        MatchingRecyclerAdapter = new MatchingRecyclerAdapter(getContext(), ProgramList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(MatchingRecyclerAdapter);
    }
}
