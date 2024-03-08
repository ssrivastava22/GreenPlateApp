package com.example.greenplate.viewmodels;
import androidx.lifecycle.ViewModel;
import java.util.Date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class InputMealViewModel extends ViewModel{

    private String name;
    private int calories;
    private Date date;

    public void InputMeal() {

    }

    public void InputMeal(String name, int calories, Date date) {
        this.name = name;
        this.calories = calories;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories(DatabaseReference userReference) {
        if (userReference != null) {
            int totalCalories = 0;
            for (Map<String, Object> meal : userReference.get("Meals")) {
                totalCalories += (int) meal.get("calories");
            }
        }
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserInfo(DatabaseReference userReference) {
        if (userReference != null) {
            int height = userReference.get("Height");
            int weight = userReference.get("Weight");
            String gender = userReference.get("Gender");
            String userInfoText = "Height: " + height + " cm\n" +
                    "Weight: " + weight + " kg\n" +
                    "Gender: " + gender);
            return userInfoText;
        } else {
            return "Failed";
        }

    }

    public int getCalorieGoal() {
        //need to write formula here
        return 100;
    }
}
