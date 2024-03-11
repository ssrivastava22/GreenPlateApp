package com.example.greenplate.model;
import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalInfoModel {
    private double height;
    private double weight;
    private String gender;
    public PersonalInfoModel(double height, double weight, String gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String getGender() { return gender; }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
}
