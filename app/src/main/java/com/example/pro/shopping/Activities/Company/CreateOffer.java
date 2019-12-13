package com.example.pro.shopping.Activities.Company;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Authentication.SignUpActivity;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.Models.Offer;
import com.example.pro.shopping.Models.User;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.example.pro.shopping.util.CustomDateTimePicker;
import com.example.pro.shopping.util.SetBtnTimeOrDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Random;

public class CreateOffer extends AppCompatActivity {

    private static final String TAG = "CreateOffer";
    private int PICK_IMAGE_REQUEST = 111;
    private Uri filePath;
    private StorageReference childRef  ;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog pd;
    private Button select_img , selectFromDate , selectFromTime , selectToDate, selectToTime ;
    private EditText desc ;
    private Offer offer  ;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://sharedprogram-5a75a.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");
        select_img =  findViewById(R.id.select_img);
        desc =  findViewById(R.id.desc);
        TimeDateActions();
        offer = new Offer();
    }

    private void TimeDateActions(){
        selectFromDate =  findViewById(R.id.selectFromDate);
        selectFromTime =  findViewById(R.id.selectFromTime);
        selectToDate =  findViewById(R.id.selectToDate);
        selectToTime =  findViewById(R.id.selectToTime);

        selectFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(CreateOffer.this);
                picker.ShowDateDialog(selectFromDate);
            }
        });
        selectFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(CreateOffer.this);
                picker.ShowTimeDialog(selectFromTime);
            }
        });

        selectToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(CreateOffer.this);
                picker.ShowDateDialog(selectToDate);
            }
        });

        selectToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(CreateOffer.this);
                picker.ShowTimeDialog(selectToTime);
            }
        });
    }

    boolean containInt(Button b){
          for (int i = 0 ; i < b.getText().toString().length() ; i++) {
              if (Character.isDigit(b.getText().toString().charAt(i))) {
                  return true;
              }
          }
      return false ;
  }


    public void SelectImg(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private boolean Validation() {
        if (filePath ==null){
            Toast.makeText(CreateOffer.this, "Please , select an image", Toast.LENGTH_SHORT).show();
            return false ;
        }

        if (!containInt(selectFromDate) || !containInt(selectToDate)|| !containInt(selectFromTime) || !containInt(selectToTime)){
            Toast.makeText(CreateOffer.this, "Please select the date ", Toast.LENGTH_SHORT).show();
            return false ;
        }

        return true ;
    }

    public void CreateAction(View view) {
        if (!Validation()) {
            return;
        }
            pd.show();
            childRef = storageRef.child(getRandomString());

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CreateOffer.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String FirePath = uri.toString() ;
                            offer.setCompany_email(REF.CUR_USER_OBJ.getEmail());
                            offer.setDesc(desc.getText().toString());
                            offer.setImagePath(FirePath);
                            User cur_user = (User)REF.CUR_USER_OBJ;
                            offer.setCompanyName(cur_user.getName());
                            offer.setFromDateTime(selectFromDate.getText().toString() + " "+ selectFromTime.getText().toString());
                            offer.setToDateTime(selectToDate.getText().toString() + " "+ selectToTime.getText().toString());
                            AddOfferFirebase();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(CreateOffer.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });

        }

    private void AddOfferFirebase() {
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.OFFERS);
        UserDatabaseReference.push().setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    CreateOffer.this.finish();
                    pd.dismiss();
                    Log.e("Success", "createProfile:Success", task.getException());
                }else {
                    pd.dismiss();
                    Log.e("failure", "createProfile:failure", task.getException());
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            String[] split = data.getDataString().split("/");
            select_img.setText(split[split.length-1]);
            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
