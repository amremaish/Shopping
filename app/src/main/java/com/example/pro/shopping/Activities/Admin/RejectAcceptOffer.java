package com.example.pro.shopping.Activities.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Offer;
import com.example.pro.shopping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

public class RejectAcceptOffer extends AppCompatActivity {
    private static final String TAG = "RejectAcceptOffer" ;
    private TextView comp_name , desc ,fromDateTime , toDateTime;
    private ImageView image_offer ;
    private DatabaseReference mDatabaseReference;
    private Bundle extras ;

    private KProgressHUD hud ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_accept_offer);
        comp_name = findViewById(R.id.comp_name);
        desc = findViewById(R.id.desc);
        image_offer = findViewById(R.id.image_offer);
        fromDateTime = findViewById(R.id.fromDateTime);
        toDateTime = findViewById(R.id.ToDateTime);
        hud = KProgressHUD.create(this) .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        hud.show();
        loadData();
    }



    private void loadData() {
         extras = getIntent().getExtras();
        comp_name.setText(extras.getString("ComapnyName"));
        desc.setText(extras.getString("Desc"));
        fromDateTime.setText( extras.getString("FromDateTime"));
        toDateTime.setText(extras.getString("ToDateTime"));
        String img_path =  extras.getString("imagePath");
        Picasso.get().load(img_path).into(image_offer, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                hud.dismiss();
            }
            @Override
            public void onError(Exception e) {
                hud.dismiss();
                Toast.makeText(RejectAcceptOffer.this,"Failed to load image.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void AcceptAction(View view ){
        SendRepsondToFirebase(Offer.ACCEPTED);
        finish();
    }

    public void RejectAction(View view ){
        SendRepsondToFirebase(Offer.REJECTED);
        finish();
    }


    private void SendRepsondToFirebase(final int Status){
        hud.show();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.OFFERS);
        UserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    Offer offer = data1.getValue(Offer.class);
                    String key =  data1.getKey();
                    if (offer.getCompany_email().equals(extras.getString("email")) ){
                        dataSnapshot.getRef().child(key).child("status").setValue(Status);
                        break;

                    }
                }
                hud.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hud.dismiss();
                Toast.makeText(RejectAcceptOffer.this,"Failed .",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
