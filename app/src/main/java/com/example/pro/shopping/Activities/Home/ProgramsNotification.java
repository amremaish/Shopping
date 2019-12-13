package com.example.pro.shopping.Activities.Home;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Home.recyclers.NotificationProgramsRecyclerAdapter;
import com.example.pro.shopping.FirebaseConstants;
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

public class ProgramsNotification extends AppCompatActivity {

    private static final String TAG = "ProgramsNotification" ;
    private android.support.v7.widget.RecyclerView RecyclerView;
    private ArrayList<Program> ProgramList;
    private ArrayList<User> UserList;
    private LinearLayout no_items ;
    private KProgressHUD hud ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs_notification);
        RecyclerView = findViewById(R.id.recycler_view);
        no_items = findViewById(R.id.no_items);
        no_items.setVisibility(View.GONE);
        hud = KProgressHUD.create(this) .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        hud.show();

        FindAllNotification();
    }


    private void FindAllNotification() {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.PROGRAM);
        UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProgramList = new ArrayList<>();

                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Program program = data1.getValue(Program.class);
                    if (program.getUserSearcherEmail().equals(REF.CUR_USER_OBJ.getEmail()) ||
                        program.getUserCreatorEmail().equals(REF.CUR_USER_OBJ.getEmail())){
                        String key =  data1.getKey();
                        if (program.getStatusRead() == Program.NOTREAD) {
                            dataSnapshot.getRef().child(key).child("statusRead").setValue(Program.READ);
                            Advertisement.counter_notification.setText("0");
                            Advertisement.counter_notification.setVisibility(View.GONE);
                        }
                        ProgramList.add(program);
                    }
                 }
                findUserFromFirebase();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProgramsNotification.this,"Failed .",Toast.LENGTH_SHORT).show();
                hud.dismiss();
            }
        });

    }

    private void findUserFromFirebase() {
        DatabaseReference  mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference  UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.USERS).child(FirebaseConstants.USERS);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserList = new ArrayList<>();
                for (int i = 0 ; i < ProgramList.size() ; i++ ) {
                        if (ProgramList.get(i).getUserSearcherEmail().equals(REF.CUR_USER_OBJ.getEmail())) {
                            if (ProgramList.get(i).getUserCreatorEmail().equals(Program.NULL)){
                                ProgramList.remove(i);
                                continue;
                            }
                            for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                                User user = data1.getValue(User.class);
                                if (ProgramList.get(i).getUserCreatorEmail().equals(user.getEmail())) {
                                    UserList.add(user);
                                }
                            }
                        } else if (ProgramList.get(i).getUserCreatorEmail().equals(REF.CUR_USER_OBJ.getEmail())){
                             if (ProgramList.get(i).getUserSearcherEmail().equals(Program.NULL)){
                                 ProgramList.remove(i);
                                 continue;
                             }
                            for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                                User user = data1.getValue(User.class);
                                if (ProgramList.get(i).getUserSearcherEmail().equals(user.getEmail())) {
                                    UserList.add(user);
                                }
                            }
                        }
                }
                addToList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
                hud.dismiss();
            }
        });

    }

    private void addToList() {
        if (ProgramList.isEmpty()) {
            no_items.setVisibility(View.VISIBLE);
        }
        hud.dismiss();
        NotificationProgramsRecyclerAdapter NotificationProgramsRecyclerAdapter = new NotificationProgramsRecyclerAdapter(this, ProgramList , UserList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(NotificationProgramsRecyclerAdapter);
    }

}