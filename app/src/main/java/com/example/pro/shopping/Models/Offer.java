package com.example.pro.shopping.Models;

import java.util.Date;

public class Offer {

    public static final int ACCEPTED = 1 , REJECTED = 2 , PENDIING = 0;
    private String FromDateTime ;
    private String ToDateTime ;
    private String desc ;
    private int status  ;
    private String CompanyName ;
    private String company_email ;
    private String imagePath ;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFromDateTime() {
        return FromDateTime;
    }

    public void setFromDateTime(String fromDateTime) {
        FromDateTime = fromDateTime;
    }

    public String getToDateTime() {
        return ToDateTime;
    }

    public void setToDateTime(String toDateTime) {
        ToDateTime = toDateTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCompany_email() {
        return company_email;
    }

    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
