package com.example.pro.shopping.Models;

import java.util.Date;

public class Program  {

    public static final int ACCEPTED = 1 , REJECTED = 2 , PENDIING = 0;
    public static final int READ = 1 , NOTREAD = 0 ;
    public static String NULL = "null";
    private String Title ;
    private String UserCreatorEmail , UserSearcherEmail = NULL;
    private String DateOfProgram ;
    private String locationOfProgram ;
    private  String TypeOfPlace ;
    private int status;
    private int StatusRead;
    private String id ;
    private boolean Riding ;

    public String getUserCreatorEmail() {
        return UserCreatorEmail;
    }

    public void setUserCreatorEmail(String userCreatorEmail) {
        UserCreatorEmail = userCreatorEmail;
    }

    public String getUserSearcherEmail() {
        return UserSearcherEmail;
    }

    public void setUserSearcherEmail(String userSearcherEmail) {
        UserSearcherEmail = userSearcherEmail;
    }

    public String getDateOfProgram() {
        return DateOfProgram;
    }

    public void setDateOfProgram(String dateOfProgram) {
        DateOfProgram = dateOfProgram;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isRiding() {
        return Riding;
    }

    public void setRiding(boolean riding) {
        Riding = riding;
    }

    public String getLocationOfProgram() {
        return locationOfProgram;
    }

    public void setLocationOfProgram(String locationOfProgram) {
        this.locationOfProgram = locationOfProgram;
    }

    public String getTypeOfPlace() {
        return TypeOfPlace;
    }

    public void setTypeOfPlace(String typeOfPlace) {
        TypeOfPlace = typeOfPlace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatusRead() {
        return StatusRead;
    }

    public void setStatusRead(int statusRead) {
        StatusRead = statusRead;
    }
}
