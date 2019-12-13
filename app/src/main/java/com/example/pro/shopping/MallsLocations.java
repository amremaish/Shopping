package com.example.pro.shopping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class MallsLocations {

    public static final String Jeddah= "Jeddah";
    public static final String Albaha= "Albaha";
    public static final String MALL= "mall";
    public static final String SUPERMARKET= "supermarket";
    LinkedHashMap<String, ArrayList<String>> JaddahMallsMap = new LinkedHashMap<>();
    LinkedHashMap<String, ArrayList<String>> AlbahaMallsMap = new LinkedHashMap<>();


    public  MallsLocations(){
        initLoactions();
    }


    private void initLoactions(){
        // Jeddah
        JaddahMallsMap  = new LinkedHashMap<>();
        JaddahMallsMap.put("Red sea mall",new ArrayList<>((Arrays.asList("21.6277686", "39.1108679" , MALL))));
        JaddahMallsMap.put("Al salaam mall",new ArrayList<>((Arrays.asList("21.5078097","39.2211443", MALL ))));
        JaddahMallsMap.put("Roshan mall",new ArrayList<>((Arrays.asList("21.6656267","39.1076917" , MALL))));
        JaddahMallsMap.put("Haifaa mall",new ArrayList<>((Arrays.asList("21.5274361","39.1751314", MALL ))));
        JaddahMallsMap.put("Mall of Arabia",new ArrayList<>((Arrays.asList("21.6324851","39.1538997", MALL ))));
        JaddahMallsMap.put("Jeddah mall",new ArrayList<>((Arrays.asList("21.548705","39.1472389" , MALL))));
        JaddahMallsMap.put("Flamingo mall",new ArrayList<>((Arrays.asList("21.5519323","39.1994174", MALL))));
        JaddahMallsMap.put("Stars avenue",new ArrayList<>((Arrays.asList("21.5767628","39.1945622" ,MALL))));
        JaddahMallsMap.put("Al-andalus mall",new ArrayList<>((Arrays.asList("21.5069328","39.2157539", MALL ))));
        JaddahMallsMap.put("Hijaz mall",new ArrayList<>((Arrays.asList("21.5913619","39.1710059" , MALL))));
        JaddahMallsMap.put("Al salad mall",new ArrayList<>((Arrays.asList("21.5078097","39.2211443", MALL ))));
        JaddahMallsMap.put("Al Jasmine mall",new ArrayList<>((Arrays.asList("21.5931404","39.2264405", MALL ))));
        JaddahMallsMap.put("Arab mall",new ArrayList<>((Arrays.asList("21.6335019","39.155998", MALL ))));
        JaddahMallsMap.put("Al balad mall",new ArrayList<>((Arrays.asList("21.4867371","39.1807692", MALL ))));
        //Albaha
        AlbahaMallsMap  = new LinkedHashMap<>();
        AlbahaMallsMap.put("Center Point",new ArrayList<>((Arrays.asList("20.0178079", "41.465677", MALL ))));
        AlbahaMallsMap.put("Ghunaim mall",new ArrayList<>((Arrays.asList("20.007077", "41.4494506" , MALL))));
        AlbahaMallsMap.put("Women's market",new ArrayList<>((Arrays.asList("20.005958", "41.4599052",SUPERMARKET ))));
        AlbahaMallsMap.put("Farm market",new ArrayList<>((Arrays.asList("20.0241999", "41.4745749", SUPERMARKET ))));
        AlbahaMallsMap.put("Huda mall women",new ArrayList<>((Arrays.asList("20.0137659", "41.4624345" , MALL))));
    }

    public LinkedHashMap<String, ArrayList<String>> getJaddahMallsMap() {
        return JaddahMallsMap;
    }

    public void setJaddahMallsMap(LinkedHashMap<String, ArrayList<String>> jaddahMallsMap) {
        JaddahMallsMap = jaddahMallsMap;
    }

    public LinkedHashMap<String, ArrayList<String>> getAlbahaMallsMap() {
        return AlbahaMallsMap;
    }

    public void setAlbahaMallsMap(LinkedHashMap<String, ArrayList<String>> albahaMallsMap) {
        AlbahaMallsMap = albahaMallsMap;
    }
}
