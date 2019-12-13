package com.example.pro.shopping.Activities.Home;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pro.shopping.Activities.Company.CreateOffer;
import com.example.pro.shopping.MallsLocations;
import com.example.pro.shopping.R;
import com.example.pro.shopping.util.CustomDateTimePicker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Search extends Fragment {

    private  View view ;
    private MallsLocations malls;
    private Button  selectFromDate , selectFromTime , selectToDate, selectToTime ;
    private RadioButton mall_radio , male_radio;
    private EditText age_txt;
    private Spinner city_spinner ,place_spinner ;
    private ImageView show_on_map ;
    private String curLong , curLatit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_search, container, false);
        inflateUI();
        mapInit();
        spinner();
        TimeDateActions();
        Actions();
        return view;
    }
    boolean containInt(Button b){
        for (int i = 0 ; i < b.getText().toString().length() ; i++) {
            if (Character.isDigit(b.getText().toString().charAt(i))) {
                return true;
            }
        }
        return false ;
    }

    private boolean isValidAge(String age){
        int iAge = 0;
        try{
             iAge = Integer.parseInt(age.trim()) ;
             if (iAge > 100 ||iAge < 5){
                 Toast.makeText(getActivity(), "check age is valid.",  Toast.LENGTH_SHORT).show();
                 return  false;
             }
        }catch (Exception x) {
            Toast.makeText(getActivity(), "check age is number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean Validation() {
        if (age_txt.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(), "please fill age field.",  Toast.LENGTH_SHORT).show();
            return false ;
        }
        if (!containInt(selectFromDate) || !containInt(selectToDate)|| !containInt(selectFromTime) || !containInt(selectToTime)){
            Toast.makeText(getContext(), "Please select the date ", Toast.LENGTH_SHORT).show();
            return false ;
        }

        if (!isValidAge(age_txt.getText().toString())){
            return false ;
        }

        return true ;
    }
    private void Actions() {
        Button searchBtn = view.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!Validation()){
                   return;
               }
                Intent intent = new Intent(getContext() , SearchResult.class);
                String isMale = male_radio.isChecked()+"" ;
                String isMall =mall_radio.isChecked()+"" ;
                intent.putExtra("gender" , mall_radio.isChecked());
                intent.putExtra("age" , age_txt.getText().toString());
                intent.putExtra("mall" , city_spinner.getSelectedItem().toString()+" " + place_spinner.getSelectedItem().toString());
                intent.putExtra("isMale" , isMale);
                intent.putExtra("isMall" , isMall);
                intent.putExtra("fromDateTime" , selectFromDate.getText().toString()+ " " + selectFromTime.getText().toString());
                intent.putExtra("toDateTime" , selectToDate.getText().toString()+ " " + selectToTime.getText().toString());
                startActivity(intent);

            }
        });
    }


    private void inflateUI() {
        place_spinner =view.findViewById(R.id.place_spinner);
        city_spinner =view.findViewById(R.id.city_spinner);
        age_txt =view.findViewById(R.id.age_txt);
        mall_radio =view.findViewById(R.id.mall_radio);
        male_radio =view.findViewById(R.id.male_radio);
        show_on_map = view.findViewById(R.id.show_on_map);
    }

    private void TimeDateActions(){
        selectFromDate =  view.findViewById(R.id.selectFromDate);
        selectFromTime =  view.findViewById(R.id.selectFromTime);
        selectToDate =  view.findViewById(R.id.selectToDate);
        selectToTime = view.findViewById(R.id.selectToTime);

        selectFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(getContext());
                picker.ShowDateDialog(selectFromDate);
            }
        });
        selectFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(getContext());
                picker.ShowTimeDialog(selectFromTime);
            }
        });

        selectToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(getContext());
                picker.ShowDateDialog(selectToDate);
            }
        });

        selectToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDateTimePicker picker = new CustomDateTimePicker(getContext());
                picker.ShowTimeDialog(selectToTime);
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
                curLatit =  posMap.get(0);
                curLong  =  posMap.get(1) ;
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
            Toast.makeText(getActivity(), "Google map is not found.", Toast.LENGTH_SHORT).show();
        }
    }

}
