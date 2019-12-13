package com.example.pro.shopping.Activities.Authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pro.shopping.R;

import java.util.Calendar;
import java.util.Date;

public class main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date currentTime = Calendar.getInstance().getTime();
//        Toast.makeText(main.this, currentTime.toString(),
//                Toast.LENGTH_SHORT).show();

    }

    public void signInPage(View view){
      startActivity(new Intent(this, SignInActivity.class));
    }
    public void signUpPage(View view){
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
