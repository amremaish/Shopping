package com.example.pro.shopping.Activities.Authentication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Company.CreateOffer;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.User;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.example.pro.shopping.util.CustomDateTimePicker;
import com.example.pro.shopping.util.SetBtnTimeOrDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG ="FirebaseAuthentication" ;
    // firebase const
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private KProgressHUD hud;
    private User NewUser ;
    private Button SelectBirthDayBtn ;
    private EditText sign_up_email , sign_up_name,sign_up_password,sign_up_confirm_password;
    private RadioButton male_radio ,hyper_active_radio,active_radio ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Returns an instance of this class corresponding to the default FirebaseApp instance.
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Returns the currently signed-in FirebaseUser or null if there is none.
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        // referense to the data base to be able to write and read to and from databas
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SelectBirthDayBtn = findViewById(R.id.SelectBirthDayBtn);
        SelectBirthDayBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelectBirthDay();
            }
        });
        NewUser = new User();
        inflateUiElements();
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
    }

    public void SelectBirthDay() {
        CustomDateTimePicker picker = new CustomDateTimePicker(SignUpActivity.this);
        picker.ShowDateDialog(SelectBirthDayBtn);

    }

    public void inflateUiElements(){
        SelectBirthDayBtn = findViewById(R.id.SelectBirthDayBtn);
        sign_up_email = findViewById(R.id.sign_up_email);
        sign_up_name = findViewById(R.id.sign_up_name);
        sign_up_password = findViewById(R.id.sign_up_password);
        sign_up_confirm_password = findViewById(R.id.sign_up_confirm_password);
        male_radio  = findViewById(R.id.male_radio);
        hyper_active_radio  = findViewById(R.id.hyper_active_radio);
        active_radio  = findViewById(R.id.active_radio);
    }

    private void GatherData() {
        NewUser.setEmail(sign_up_email.getText().toString());
        NewUser.setPassword(sign_up_password.getText().toString());
        NewUser.setBirthDate(CustomDateTimePicker.MONTH+"-"+ CustomDateTimePicker.DAY+"-"+ CustomDateTimePicker.YEAR);
        NewUser.setUserType(REF.USER);
        NewUser.setName(sign_up_name.getText().toString());
        NewUser.setProgramsNumber(0);
        NewUser.setRating(0);

        String nature ;
        if (hyper_active_radio.isChecked())
            nature = User.HYPER;
        else if (active_radio.isChecked()){
            nature =User.ACTIVE ;
        }else {
            nature =User.CALM ;
        }
        NewUser.setNature(nature);
        if (male_radio.isChecked())
            NewUser.setGender(User.MALE);
        else
            NewUser.setGender(User.FEMALE);
    }

    private boolean IsValidateInfo(){
        if (TextUtils.isEmpty(sign_up_name.getText().toString())) {
            Toast.makeText(this, "Please write your name ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(sign_up_email.getText().toString())) {
            Toast.makeText(this, "Please write your email ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(sign_up_password.getText().toString())) {
            Toast.makeText(this, "Please write the password ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(sign_up_confirm_password.getText().toString())) {
            Toast.makeText(this, "Please write the confirm password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!sign_up_password.getText().toString().equals(sign_up_confirm_password.getText().toString())) {
            Toast.makeText(this, "the confirm password doesn't match and the password", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!isValidEmailAddress(sign_up_email.getText().toString())) {
            Toast.makeText(this, "please check the email ", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (sign_up_password.getText().toString().length() < 6) {
            Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (CustomDateTimePicker.YEAR ==0 ){
            Toast.makeText(this, "Please select your birthday", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true ;
    }

    public void SignUpAction(View view) {
            if (!IsValidateInfo()){
                return ;
           }
           hud.show();
        GatherData();
        createAccountOnFirebaseAuthentication(sign_up_email.getText().toString() ,sign_up_password.getText().toString() );
    }

    public void createAccountOnFirebaseAuthentication(String email,String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            createProfileOnFirebaseDatabase(NewUser , user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            hud.dismiss();
                        }
                    }
                });
    }

    public void createProfileOnFirebaseDatabase(final User NewUser , String userID ){
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.USERS).child(FirebaseConstants.USERS).child(userID);
        UserDatabaseReference.setValue(NewUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    hud.dismiss();
                    SignUpActivity.this.finish();
                    Toast.makeText(SignUpActivity.this, "Successfully registered.", Toast.LENGTH_LONG).show();

                }else {
                    Log.e(TAG, "createProfile:failure", task.getException());
                    hud.dismiss();
                }
            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
