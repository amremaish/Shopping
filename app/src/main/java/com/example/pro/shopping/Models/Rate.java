package com.example.pro.shopping.Models;

public class Rate {

    private String ratedEmail , rateFromEmail ;
    private float rating ;


    public String getRatedEmail() {
        return ratedEmail;
    }

    public void setRatedEmail(String ratedEmail) {
        this.ratedEmail = ratedEmail;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getRateFromEmail() {
        return rateFromEmail;
    }

    public void setRateFromEmail(String rateFromEmail) {
        this.rateFromEmail = rateFromEmail;
    }
}
