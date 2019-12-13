package com.example.pro.shopping.Activities.Home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.pro.shopping.Activities.Home.recyclers.SearchResultRecyclerAdapter;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.MallsLocations;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchResult extends AppCompatActivity {
    private String   age , place , Gender , TypeOfPlace , fromDateTime , toDateTime;
    private android.support.v7.widget.RecyclerView RecyclerView;
    private SearchResultRecyclerAdapter SearchResultRecyclerAdapter;
    private ArrayList<Program>ProgramList;
    private ArrayList<User>UsersList;
    private DatabaseReference mDatabaseReference;
    private KProgressHUD hud ;
    private LinearLayout no_items;
    public static SearchResult SearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        this.SearchResult = this;
        no_items = findViewById(R.id.no_items);
        RecyclerView = findViewById(R.id.recycler_view);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        no_items.setVisibility(View.GONE);
        getDataSearch();
        findDatafromFirebase();
    }

    private void getDataSearch(){
        Intent intent =getIntent();
        age = intent.getStringExtra("age");
        place = intent.getStringExtra("mall");
        String isMale = intent.getStringExtra("isMale");
        String isMall = intent.getStringExtra("isMall");
        if(isMall.equals("true")){
            TypeOfPlace = MallsLocations.MALL;
        }else {
            TypeOfPlace = MallsLocations.SUPERMARKET;
        }
         if(isMale.equals("true")){
             Gender = User.MALE;
         }else {
             Gender = User.FEMALE;
         }
         fromDateTime = intent.getStringExtra("fromDateTime");
         toDateTime = intent.getStringExtra("toDateTime");

    }


    private boolean dateValidation(Program program ){
       try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");
            Date convertedDateOfProgram = dateFormat.parse(program.getDateOfProgram());
            Date convertedFromDateTime = dateFormat.parse(fromDateTime);
            Date convertedToDateTime = dateFormat.parse(toDateTime);
            long DateOfProgram = convertedDateOfProgram.getTime();
            long FromDateTime = convertedFromDateTime.getTime();
            long ToDateTime = convertedToDateTime.getTime();

            if (DateOfProgram >= FromDateTime && DateOfProgram <= ToDateTime){
                return true ;
            }

        } catch (ParseException e) {
              e.printStackTrace();
        }

    return false ;
    }

    private void findDatafromFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.PROGRAM);
        UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProgramList = new ArrayList<>();
                UsersList = new ArrayList<>();
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Program program = data1.getValue(Program.class);
                    if (program.getUserSearcherEmail().equals(Program.NULL)
                            && dateValidation(program)
                            && TypeOfPlace.equals(program.getTypeOfPlace())
                            && place.equals(program.getLocationOfProgram())
                            && !program.getUserCreatorEmail().equals(REF.CUR_USER_OBJ.getEmail())){
                        getUserEmail(program.getUserCreatorEmail(), program);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchResult.this,"Failed .",Toast.LENGTH_SHORT).show();
                hud.dismiss();
            }
        });
    }

    private void getUserEmail(final String creatorEmail ,final Program program) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.USERS).child(FirebaseConstants.USERS);
        UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    User user = data1.getValue(User.class);
                    String UserAge = (2019 - Integer.parseInt(user.getBirthDate().split("-")[2]))+"";
                    if (user.getEmail().equals(creatorEmail)
                            && UserAge.equals(age)
                            && Gender.equals(user.getGender())){
                        ProgramList.add(program);
                        UsersList.add(user);
                    }
                }
                FindAllMatches();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hud.dismiss();
            }
        });
    }

    private void FindAllMatches() {

        if (ProgramList.isEmpty()){
            no_items.setVisibility(View.VISIBLE);
            return;
        }

        SearchResultRecyclerAdapter = new SearchResultRecyclerAdapter(SearchResult.this, ProgramList ,UsersList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchResult.this);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(SearchResultRecyclerAdapter);
    }


}
