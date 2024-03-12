package com.example.greenplate.views;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.greenplate.R;
import com.example.greenplate.model.InputMealModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.greenplate.viewmodels.InputMealViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import android.content.Intent;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
public class InputMealView extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private EditText editMealText;
    private EditText editCalorieText;
    private Button enterMealButton;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Meals");
    private DatabaseReference userRoot = db.getReference().child("Users");
    private InputMealViewModel viewModel;
    private TextView userInfoTextView;
    private TextView calorieGoalTextView;
    private TextView dailyCalorieIntakeTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal);
        viewModel = new ViewModelProvider(this).get(InputMealViewModel.class);
        editMealText = findViewById(R.id.InputMealName);
        editCalorieText = findViewById(R.id.InputCalories);
        enterMealButton = findViewById(R.id.InputMealButton);
        userInfoTextView = findViewById(R.id.userInfoTextView);
        calorieGoalTextView = findViewById(R.id.calorieGoalTextView);
        dailyCalorieIntakeTextView = findViewById(R.id.dailyCalorieIntakeTextView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(InputMealView.this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userRef = db.getReference().child("Users").child(currentUser.getUid());

        enterMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int calorieText = Integer.parseInt(editCalorieText.getText().toString());
                String mealName = editMealText.getText().toString();
                DatabaseReference newMealRef = root.push();
                newMealRef.child("Meal Name").setValue(mealName);
                newMealRef.child("Calories").setValue(calorieText)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(InputMealView.this, "Meal saved", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(InputMealView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });
        retrieveUserInfoAndCalculateCalorieGoal();
        calculateAndDisplayDailyCalorieIntake();
    }
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
            int id = item.getItemId();
            if (id == R.id.Home) {
                startActivity(new Intent(InputMealView.this, Home.class));
                return true;
            } else if (id == R.id.Recipe) {
                startActivity(new Intent(InputMealView.this, RecipeView.class));
                return true;
            } else if (id == R.id.Ingredients) {
                startActivity(new Intent(InputMealView.this, IngredientsView.class));
                return true;
            } else if (id == R.id.ShoppingList) {
                startActivity(new Intent(InputMealView.this, ShoppingListView.class));
                return true;
            }  else if (id == R.id.InputMeal) {
                return true;
            }
            return false;
        }
    private void retrieveUserInfoAndCalculateCalorieGoal() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String gender = dataSnapshot.child("Gender").getValue(String.class);
                    int height = dataSnapshot.child("Height").getValue(Integer.class);
                    int weight = dataSnapshot.child("Weight").getValue(Integer.class);

                    userInfoTextView.setText("Gender: " + gender + ", Height: " + height + " cm, Weight: " + weight + " kg");

                    double calorieGoal = calculateCalorieGoal(Gender, Height, Weight);
                    calorieGoalTextView.setText("Calorie Goal: " + calorieGoal + " kcal");
                } else {
                    //Default values
                    userInfoTextView.setText("John");
                    calorieGoalTextView.setText("100");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", "Error: " + databaseError.getMessage());
            }
        });
    }

    private double calculateCalorieGoal(String gender, int height, int weight) {
        double bmr;
        if (gender.equalsIgnoreCase("Male")) {
            bmr = 66 + (13.75 * weight) + (5 * height);
        } else if (gender.equalsIgnoreCase("Female")) {
            bmr = 655 + (9.56 * weight) + (1.85 * height);
        } else {
            throw new IllegalArgumentException("Invalid gender specified.");
        }
        return bmr;
    }

    private void calculateAndDisplayDailyCalorieIntake() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDateStr = dateFormat.format(currentDate);

        userRef.orderByChild("Date").equalTo(currentDateStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalCalories = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                        int calories = mealSnapshot.child("Calories").getValue(Integer.class);
                        totalCalories += calories;
                    }
                }
                dailyCalorieIntakeTextView.setText("Daily Calorie Intake: " + totalCalories + " kcal");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", "Error: " + databaseError.getMessage());
            }
        });
    }
}