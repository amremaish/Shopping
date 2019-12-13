package com.example.pro.shopping.Activities.Home;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Home.recyclers.HistoryProgramsRecyclerAdapter;
import com.example.pro.shopping.Activities.Home.recyclers.NotificationProgramsRecyclerAdapter;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Program;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

public class HistoryCreatorProgram extends AppCompatActivity {
    private LinearLayout no_items ;
    private KProgressHUD hud ;
    private ArrayList<Program> ProgramList;
    private android.support.v7.widget.RecyclerView RecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_creator_program);
        RecyclerView = findViewById(R.id.recycler_view);
        no_items = findViewById(R.id.no_items);
        no_items.setVisibility(View.GONE);
        hud = KProgressHUD.create(this) .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        hud.show();
        FindAllCreatorProram();
    }



    private void FindAllCreatorProram() {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.PROGRAM);
        UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProgramList = new ArrayList<>();

                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Program program = data1.getValue(Program.class);
                    if(program.getUserCreatorEmail().equals(REF.CUR_USER_OBJ.getEmail())){
                        ProgramList.add(program);
                    }
                }
                addToList();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HistoryCreatorProgram.this,"Failed .",Toast.LENGTH_SHORT).show();
                hud.dismiss();
            }
        });

    }


    private void addToList() {
        if (ProgramList.isEmpty()) {
            no_items.setVisibility(View.VISIBLE);
        }
        hud.dismiss();
        HistoryProgramsRecyclerAdapter HistoryProgramsRecyclerAdapter = new HistoryProgramsRecyclerAdapter(this, ProgramList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(HistoryProgramsRecyclerAdapter);
    }
}
