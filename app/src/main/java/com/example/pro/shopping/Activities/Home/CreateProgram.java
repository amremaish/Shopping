package com.example.pro.shopping.Activities.Home;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Authentication.SignInActivity;
import com.example.pro.shopping.Activities.Company.CreateOffer;
import com.example.pro.shopping.FirebaseConstants;
import com.example.pro.shopping.MallsLocations;
import com.example.pro.shopping.Models.Program;
import com.example.pro.shopping.R;
import com.example.pro.shopping.REF;
import com.example.pro.shopping.util.CustomDateTimePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CreateProgram extends Fragment {

    private Spinner city_spinner, place_spinner;
    private ImageView show_on_map ;
    private MallsLocations malls;
    private String curLong , curLatit;
    private EditText program_title ;
    private Button  selectDate , selectTime ;
    private CheckBox ride_sharing_box ;
    private DatabaseReference mDatabaseReference;
    private Button search_btn ;
    private Program program ;
    private KProgressHUD hud ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_create_program, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
       program = new Program();
       TimeDateActions(view);
       InflateUI(view);
       mapInit();
       spinner();
       Actions();
       hud = KProgressHUD.create(getContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
       return view ;
    }
    private void TimeDateActions(View view ){

        selectDate =  view.findViewById(R.id.selectDate);
        selectTime =  view.findViewById(R.id.selectTime);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(CreateProgram.this.getContext());
                picker.ShowDateDialog(selectDate);
            }
        });
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(CreateProgram.this.getContext());
                picker.ShowTimeDialog(selectTime);
            }
        });

    }

    private void InflateUI(View view) {
        city_spinner = view.findViewById(R.id.city_spinner);
        place_spinner = view.findViewById(R.id.place_spinner);
        show_on_map = view.findViewById(R.id.show_on_map);
        program_title = view.findViewById(R.id.program_title);
        ride_sharing_box = view.findViewById(R.id.ride_sharing_box);
        search_btn = view.findViewById(R.id.search_btn);

    }

    private void Actions(){
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search();
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

    public boolean isFormValid() {

        if (program_title.getText().toString().trim().isEmpty()){
            Toast.makeText(CreateProgram.this.getContext(), "Please fill program title .", Toast.LENGTH_SHORT).show();
            return false ;
        }

        if (!containInt(selectDate) || !containInt(selectTime)){
            Toast.makeText(CreateProgram.this.getContext(), "Please select the date or time ", Toast.LENGTH_SHORT).show();
            return false ;
        }

       return true ;
    }

    public void Search() {
       if (!isFormValid()){
           return ;
       }
        hud.show();
         // fill data
        program.setUserCreatorEmail(REF.CUR_USER_OBJ.getEmail());
        program.setStatus(Program.PENDIING);
        String datTime  = selectDate.getText().toString() + " "+ selectTime.getText().toString() ;
        program.setDateOfProgram(datTime);
        program.setStatusRead(Program.NOTREAD);
        program.setId(getRandomString());
        program.setTitle(program_title.getText().toString());
        program.setRiding(ride_sharing_box.isChecked());
        program.setLocationOfProgram(city_spinner.getSelectedItem().toString()+" "+place_spinner.getSelectedItem().toString());
        DatabaseReference UserDatabaseReference = mDatabaseReference.child(FirebaseConstants.PROGRAM);
        UserDatabaseReference.push().setValue(program).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    hud.dismiss();
                    Toast.makeText(CreateProgram.this.getContext(), "Created Successfully", Toast.LENGTH_SHORT).show();
                    Log.e("Success", "createProfile:Success", task.getException());
                }else {
                    hud.dismiss();
                    Toast.makeText(CreateProgram.this.getContext(), "Failed !!", Toast.LENGTH_SHORT).show();
                    Log.e("failure", "createProfile:failure", task.getException());
                }
            }
        });
    }

    private void mapInit() {
        malls = new MallsLocations();
        curLong =  malls.getJaddahMallsMap().get("Red sea mall").get(1);
        curLatit = malls.getJaddahMallsMap().get("Red sea mall").get(0);
        show_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap(Uri.parse("geo: "+curLatit+","+curLong));
            }
        });
    }

    private void spinner() {
        final ArrayList<String> cities = new ArrayList<>();
        cities.add(MallsLocations.Jeddah);
        cities.add(MallsLocations.Albaha);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, cities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(dataAdapter);

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               String curCity =  cities.get(position);
                if (curCity .equals(MallsLocations.Jeddah)){
                    addMalls(malls.getJaddahMallsMap());
                }else {
                    addMalls(malls.getAlbahaMallsMap());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void addMalls(final LinkedHashMap<String, ArrayList<String>> MallsList){
        final List<String> List = new ArrayList<>();
        for (String key : MallsList.keySet())
            List.add(key);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, List);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        place_spinner.setAdapter(dataAdapter);

        place_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String curCity =  List.get(position);
                ArrayList<String>posMap = MallsList.get(curCity);
                curLatit =posMap.get(0);
                curLong =posMap.get(1) ;
                program.setTypeOfPlace(posMap.get(2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else{
            Toast.makeText(getActivity(), "Google map not found.",
                    Toast.LENGTH_SHORT).show();
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




