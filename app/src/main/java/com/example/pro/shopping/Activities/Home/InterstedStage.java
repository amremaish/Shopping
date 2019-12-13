package com.example.pro.shopping.Activities.Home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pro.shopping.R;

public class InterstedStage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intersted_stage);
        String name = getIntent().getStringExtra("name");
        TextView user_name_txt = findViewById(R.id.user_name_txt);
        user_name_txt.setText(name);

    }


    public void OKAction(View view) {
        this.finish();
        SearchResult.SearchResult.finish();
    }
}
