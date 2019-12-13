package com.example.pro.shopping.Activities.Authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Admin.Admin;
import com.example.pro.shopping.Activities.Company.Company;
import com.example.pro.shopping.Activities.Home.Home;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.User;
import com.example.pro.shopping.REF;
import com.example.pro.shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

public class SignInActivity extends AppCompatActivity {
    private KProgressHUD hud ;
    private static final String TAG = "SignInActivity";
    private TextInputEditText email;
    private TextInputEditText password;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mFirebaseAuth = FirebaseAuth.getInstance();
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
    }

    public void signInPage(View view) {
        hud.show();
        email = (TextInputEditText)findViewById(R.id.sign_in_page_email);
        password = (TextInputEditText)findViewById(R.id.sign_in_page_password);
        AuthLogin(email.getText().toString(),password.getText().toString());

    }

    private void  AuthLogin(String email , String password){
        mFirebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();

                            loginAsUser(user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            hud.dismiss();
                        }
                    }
                });

    }



    public void loginAsUser(final String userID){
        DatabaseReference UserDatabaseReference = null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.USERS).child(FirebaseConstants.USERS).child(userID);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user == null){
                    loginAsCompany(userID);
                }else {
                    hud.dismiss();
                    REF.CUR_USER = REF.USER;
                    REF.CUR_USER_OBJ = user;
                    startActivity( new Intent(SignInActivity.this , Home.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                hud.dismiss(); Log.d(TAG, "onCancelled: ");
            }
        });
    }
    public void loginAsCompany(final String userID){
        DatabaseReference UserDatabaseReference = null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.USERS).child(FirebaseConstants.CAMPANIES).child(userID);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                com.example.pro.shopping.Models.User user = dataSnapshot.getValue(com.example.pro.shopping.Models.User.class);
                if (user == null){
                    loginAsAdmin(userID);
                }else {
                    hud.dismiss();
                    REF.CUR_USER = REF.COMPANY;
                    REF.CUR_USER_OBJ = user;
                    startActivity( new Intent(SignInActivity.this , Company.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                hud.dismiss(); Log.d(TAG, "onCancelled: ");
            }
        });
    }

    public void loginAsAdmin(final String userID){
        DatabaseReference UserDatabaseReference = null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.USERS).child(FirebaseConstants.ADMIN).child(userID);
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null){
                    Toast.makeText(SignInActivity.this, "incorrect your email or password.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    hud.dismiss();
                    REF.CUR_USER = REF.ADMIN;
                    REF.CUR_USER_OBJ = user;
                    startActivity( new Intent(SignInActivity.this , Admin.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                hud.dismiss();
                Log.d(TAG, "onCancelled: ");
            }
        });
    }

    public void forgetPasswordAcrion(View view) {
        startActivity( new Intent(this , forgetPassword.class));
    }
}
