package com.example.greenplate.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenplate.model.InputMealModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;

public class InputMealViewModel extends ViewModel {
    private InputMealModel meal;
    private DatabaseReference mealRef;
    private MutableLiveData<List<Integer>> dailyCaloricIntake;

    public InputMealViewModel() {
        meal = new InputMealModel("", 0);
        mealRef = FirebaseDatabase.getInstance().getReference().child("Meals");
        dailyCaloricIntake = new MutableLiveData<>();
    }

    public LiveData<List<Integer>> getDailyCaloricIntake() {
        return dailyCaloricIntake;
    }

    public void fetchDailyCaloricIntake() {
        int testYear = 2024; // Temporary hardcoded value for testing
        int testMonth = 11;  // November, also for testing

        mealRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Integer> dailyIntake = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dateString = snapshot.child("Date").getValue(String.class);
                    if (dateString != null) {
                        try {
                            Date date = sdf.parse(dateString);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH) + 1; // January is 0 in Calendar

                            if (year == testYear && month == testMonth) {
                                Long caloriesLong = snapshot.child("Calories").getValue(Long.class); // Corrected line
                                if (caloriesLong != null) {
                                    int calories = caloriesLong.intValue(); // Convert Long to int
                                    dailyIntake.add(calories); // Add to list
                                }
                            }
                        } catch (ParseException e) {
                            Log.e("DataRetrieval", "Date parsing error", e);
                        }
                    }
                }
                dailyCaloricIntake.setValue(dailyIntake);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DataRetrieval", "Data fetch cancelled or error occurred. Error: " + databaseError.getMessage());
            }
        });
    }



//    public void fetchDailyCaloricIntake() {
//        Calendar calendar = Calendar.getInstance();
//        int currentYear = calendar.get(Calendar.YEAR);
//        int currentMonth = calendar.get(Calendar.MONTH) + 1;
//        mealRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Integer> dailyIntake = new ArrayList<>();
//                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String dateString = snapshot.child("Date").getValue(String.class);
//                    if (dateString != null) {
//                        try {
//                            Date date = sdf.parse(dateString);
//                            Calendar cal = Calendar.getInstance();
//                            cal.setTime(date);
//                            int year = cal.get(Calendar.YEAR);
//                            int month = cal.get(Calendar.MONTH) + 1;
//
//                            if (year == currentYear && month == currentMonth) {
//                                String caloriesString = snapshot.child("Calories").getValue(String.class);
//                                if (caloriesString != null) {
//                                    int calories = Integer.parseInt(caloriesString.trim());
//                                    dailyIntake.add(calories);
//                                }
//                            }
//                        } catch (ParseException e) {
//                            Log.e("DataRetrieval", "Date parsing error", e);
//                        } catch (NumberFormatException e) {
//                            Log.e("DataRetrieval", "Invalid format for Calories, must be an integer.", e);
//                        }
//                    }
//                }
//                dailyCaloricIntake.setValue(dailyIntake);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("DataRetrieval", "Data fetch cancelled or error occurred. Error: " + databaseError.getMessage());
//            }
//        });
//    }
}
