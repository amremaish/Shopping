package com.example.pro.shopping.Models;

import java.util.Date;

public class User extends UserLogin {

    public static final String HYPER = "Hyper Active"  , ACTIVE = "Active" , CALM = "calm";
    public static final String MALE = "Male"  , FEMALE = "Female" ;
    private int ProgramsNumber ;
    private int Rating ;
    private String birthDate ;
    private String UserType ;
    private String Gender ;
    private String Nature;
    private String name ;

    public int getProgramsNumber() {
        return ProgramsNumber;
    }

    public void setProgramsNumber(int programsNumber) {
        ProgramsNumber = programsNumber;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String nature) {
        Nature = nature;
    }



}
