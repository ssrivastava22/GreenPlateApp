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
import com.example.greenplate.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.greenplate.viewmodels.InputMealViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import android.content.Intent;
import android.view.MenuItem;
import com.example.greenplate.model.MealInfo;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.content.Intent;
import android.view.MenuItem;
import androidx.lifecycle.Observer;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.charts.Cartesian;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.example.greenplate.viewmodels.InputMealViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InputMealView extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener{
    private EditText editMealText;
    private EditText editCalorieText;
    private Button enterMealButton;
    private EditText editDateText;

    private Button button1;
    private Button button2;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Meal");
    private InputMealViewModel viewModel;
    private String mealKey;

    private TextView userInfoTextView;
    private TextView calorieGoalTextView;
    private TextView dailyCalorieIntakeTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private DatabaseReference mealsRef;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputmeal);
        viewModel = new ViewModelProvider(this).get(InputMealViewModel.class);
        editMealText = findViewById(R.id.InputMealName);
        editCalorieText = findViewById(R.id.InputCalories);
        editDateText = findViewById(R.id.InputDate);
        enterMealButton = findViewById(R.id.InputMealButton);
        button1 = findViewById(R.id.visualizeButton1);
        button2 = findViewById(R.id.visualizeButton2);
        userInfoTextView = findViewById(R.id.userInfoTextView);
        calorieGoalTextView = findViewById(R.id.calorieGoalTextView);
        dailyCalorieIntakeTextView = findViewById(R.id.dailyCalorieIntakeTextView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("InputMealView", "Visualize button clicked");
                viewModel.fetchDailyCaloricIntake();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("InputMealView", "Visualize button clicked");
                viewModel.fetchDailyCaloricIntake();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("InputMealView", "Visualize button 2 clicked");
            }
        });

        viewModel.getDailyCaloricIntake().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> dailyCaloricIntake) {
                if (dailyCaloricIntake != null && !dailyCaloricIntake.isEmpty()) {
                    Log.d("InputMealView", "Data received: " + dailyCaloricIntake.toString());
                    createCaloricIntakeChart(dailyCaloricIntake);
                } else {
                    Log.d("InputMealView", "No data received or data is empty");
                }
            }
        });
        user = User.getInstance();
        String username = user.getUsername();
        String sanitizedUsername = username.split("@")[0].replaceAll("[.#$\\[\\]]", "");
        userRef = db.getReference().child("Users").child(sanitizedUsername);
        mealsRef = db.getReference().child("Users").child(sanitizedUsername).child("Meals");
        enterMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mealName = editMealText.getText().toString().trim();
                String calorieText = editCalorieText.getText().toString().trim();
                String date = editDateText.getText().toString().trim();

                if (mealName.isEmpty()) {
                    Toast.makeText(InputMealView.this, "Meal Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (calorieText.isEmpty()) {
                    Toast.makeText(InputMealView.this, "Calories field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (date.isEmpty()) {
                    Toast.makeText(InputMealView.this, "Date cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (date.length() != 10) {
                    Toast.makeText(InputMealView.this, "Date is in invalid format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the current user's username
                String username = user.getUsername();
                if(username == null || username.isEmpty()) {
                    Toast.makeText(InputMealView.this, "User is not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sanitize the username to create a valid Firebase key
                String sanitizedUsername = username.split("@")[0].replaceAll("[.#$\\[\\]]", "");

                // Locate the user's entry in the "Users" table and add the meal information
                DatabaseReference userMealsRef = FirebaseDatabase.getInstance().getReference("Users").child(sanitizedUsername).child("Meals");

                try {
                    int calorieValue = Integer.parseInt(calorieText);
                    MealInfo mealInfo = new MealInfo(mealName, calorieValue, date); // Assuming you have a MealInfo class that represents the structure of a meal

                    // Generate a unique key for the new meal entry
                    String mealKey = userMealsRef.push().getKey();
                    if (mealKey != null) {
                        userMealsRef.child(mealKey).setValue(mealInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(InputMealView.this, "Meal added to user profile", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(InputMealView.this, "Failed to add meal to user profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(InputMealView.this, "Invalid Calorie Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //retrieveUserInfoAndCalculateCalorieGoal();
        //calculateAndDisplayDailyCalorieIntake();

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
            } else if (id == R.id.InputMeal) {
                //startActivity(new Intent(InputMealView.this, ShoppingListView.class));
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

                    double calorieGoal = calculateCalorieGoal(gender, height, weight);
                    calorieGoalTextView.setText("Calorie Goal: " + calorieGoal + " kcal");
                } else {
                    //Default values
                    userInfoTextView.setText("John");
                    calorieGoalTextView.setText("1000");
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

        mealsRef.orderByChild("Date").equalTo(currentDateStr).addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void createCaloricIntakeChart(List<Integer> dailyCaloricIntake) {
        Cartesian columnChart = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < dailyCaloricIntake.size(); i++) {
            data.add(new ValueDataEntry("Day " + (i + 1), dailyCaloricIntake.get(i)));
        }
        columnChart.data(data);
        columnChart.title("Daily Caloric Intake Over Past Month");
        columnChart.xAxis(0).title("Day");
        columnChart.yAxis(0).title("Calories");
        //AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        //anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        //anyChartView.setChart(columnChart);
    }
}


