package com.example.pro.shopping.Activities.Authentication;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Home.Home;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.User;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.example.pro.shopping.mail.mail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

public class forgetPassword extends AppCompatActivity {

    private static final String TAG = "test";
    private String username ;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private TextInputEditText email_text ;
    private KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        email_text = findViewById(R.id.email_text);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
    }

    public void SendMail(String email, String pass , String name) {
        mail mail = new mail();
        String msg = "Hello "+ name+"\n"+
                "Your forgotten Password is " + pass + " .\n" +
                "SSA Team";
        mail.sendMail(email, "Reset password from SSA", msg);
    }

    public void resetPassword(View view) {
        hud.show();
        findEmail();
    }


    private void findEmail(){
        DatabaseReference UserDatabaseReference = null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.USERS).child(FirebaseConstants.USERS);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    User user1 = data1.getValue(User.class);
                    if (email_text.getText().toString().equals(user1.getEmail())){
                        SendMail(user1.getEmail(),user1.getPassword() ,user1.getName() );
                        hud.dismiss();
                        forgetPassword.this.finish();
                        Toast.makeText(forgetPassword.this,"The password was successfully sent.",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                }
                Toast.makeText(forgetPassword.this,"Your email isn't exist",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hud.dismiss(); Log.d(TAG, "onCancelled: ");
            }
        });


    }
}
