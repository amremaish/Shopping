package com.example.pro.shopping.Activities.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pro.shopping.R;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

public class offer_details extends AppCompatActivity {

    private TextView comp_name , desc ,fromDateTime , toDateTime;
    private ImageView image_offer ;
    private KProgressHUD hud ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
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
        Bundle extras = getIntent().getExtras();
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
                Toast.makeText(offer_details.this,"Failed to load image.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finish(View view) {
        finish();
    }
}
