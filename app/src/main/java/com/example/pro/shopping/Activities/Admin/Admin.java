package com.example.pro.shopping.Activities.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pro.shopping.R;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void pending_offers_action(View view) {
        startActivity( new Intent(this , PendingOffers.class));
    }

    public void offers_history_action(View view) {
        startActivity( new Intent(this , OffersHistory.class));
    }


    public void view_profile(View view) {

        startActivity( new Intent(this , ProfileAdmin.class));
    }
}
