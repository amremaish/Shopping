package com.example.pro.shopping.Activities.Company;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pro.shopping.Activities.Admin.OffersHistory;
import com.example.pro.shopping.Activities.Admin.PendingOffers;
import com.example.pro.shopping.R;

public class Company extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
    }
    public void offers_history_action(View view) {
        startActivity( new Intent(this , OffersHistory.class));
    }

    public void create_offer_action(View view) {
        startActivity( new Intent(this , CreateOffer.class));
    }
}
