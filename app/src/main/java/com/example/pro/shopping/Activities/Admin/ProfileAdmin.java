package com.example.pro.shopping.Activities.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pro.shopping.Models.User;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.kaopiz.kprogresshud.KProgressHUD;

public class ProfileAdmin extends AppCompatActivity {
    private User CurUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_admin);
        TextView email = findViewById(R.id.email);
        TextView age =  findViewById(R.id.age);
        TextView name =  findViewById(R.id.name);
        TextView gender =  findViewById(R.id.gender);
        CurUser = (User) REF.CUR_USER_OBJ;
        email.setText(CurUser.getEmail());
        age.setText(CurUser.getBirthDate());
        name.setText(CurUser.getName());
        gender.setText(CurUser.getGender());

    }


}
