// package com.example.greenplate.views;
// import android.os.Bundle;
// import android.view.View;
// import android.widget.Button;
// import android.widget.EditText;
// import android.widget.Toast;
// import androidx.annotation.NonNull;
// import androidx.appcompat.app.AppCompatActivity;
// import androidx.lifecycle.ViewModelProvider;
// import com.example.greenplate.R;
// import com.google.android.gms.tasks.OnFailureListener;
// import com.google.android.gms.tasks.OnSuccessListener;
// import com.google.android.material.bottomnavigation.BottomNavigationView;
// import com.example.greenplate.viewmodels.InputMealViewModel;
// import com.google.android.gms.tasks.OnCompleteListener;
// import com.google.firebase.database.FirebaseDatabase;
// import com.google.firebase.database.DatabaseReference;
// import android.content.Intent;
// import android.view.MenuItem;
// public class InputMealView extends AppCompatActivity implements
//         BottomNavigationView.OnNavigationItemSelectedListener{
//     private EditText editMealText;
//     private EditText editCalorieText;
//     private Button enterMealButton;
//     private EditText editDateText;

//     private FirebaseDatabase db = FirebaseDatabase.getInstance();
//     private DatabaseReference root = db.getReference().child("Meals");
//     private InputMealViewModel viewModel;

//     @Override
//     protected void onCreate(Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_input_meal);
//         viewModel = new ViewModelProvider(this).get(InputMealViewModel.class);
//         editMealText = findViewById(R.id.InputMealName);
//         editCalorieText = findViewById(R.id.InputCalories);
//         editDateText = findViewById(R.id.InputDate);
//         enterMealButton = findViewById(R.id.InputMealButton);
//         BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//         bottomNavigationView.setOnNavigationItemSelectedListener(this);
//         enterMealButton.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 String calorieText =editCalorieText.getText().toString();
//                 String mealName = editMealText.getText().toString();
//                 String date = editDateText.getText().toString();
//                 if (mealName.isEmpty()) {
//                     Toast.makeText(InputMealView.this, "The 'Meal Name' cannot be empty", Toast.LENGTH_SHORT).show();
//                 } else if (calorieText.isEmpty()) {
//                     Toast.makeText(InputMealView.this, "Calories field cannot be empty", Toast.LENGTH_SHORT).show();
//                 } else if (date.isEmpty()) {
//                     Toast.makeText(InputMealView.this, "Date cannot be empty", Toast.LENGTH_SHORT).show();
//                 } else if (date.length() != 10) {
//                     Toast.makeText(InputMealView.this, "Date is in invalid format", Toast.LENGTH_SHORT).show();
//                     Toast.makeText(InputMealView.this, "The 'Calories' field cannot be empty", Toast.LENGTH_SHORT).show();
//                 } else {
//                     try {
//                         int calorieValue = Integer.parseInt(calorieText);
//                         DatabaseReference newMealRef = root.push();
//                         newMealRef.child("Meal Name").setValue(mealName);
//                         newMealRef.child("Date").setValue(date);
//                         newMealRef.child("Calories").setValue(calorieValue)
//                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                     @Override
//                                     public void onSuccess(Void unused) {
//                                         Toast.makeText(InputMealView.this, "Meal saved!", Toast.LENGTH_SHORT).show();
//                                     }
//                                 })
//                                 .addOnFailureListener(new OnFailureListener() {
//                                     @Override
//                                     public void onFailure(@NonNull Exception e) {
//                                         Toast.makeText(InputMealView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                     }
//                                 });
//                     } catch (NumberFormatException e) {
//                         Toast.makeText(InputMealView.this, "Invalid Calorie Input", Toast.LENGTH_SHORT).show();
//                     }

//     // will need to be updated once personal info screen gets created
//     @Override
//     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//         int id = item.getItemId();
//         if (id == R.id.Home) {
//             startActivity(new Intent(InputMealView.this, Home.class));
//             return true;
//         } else if (id == R.id.Recipe) {
//             startActivity(new Intent(InputMealView.this, RecipeView.class));
//             return true;
//         } else if (id == R.id.InputMeal) {
//             return true;
//         } else if (id == R.id.Ingredients) {
//             startActivity(new Intent(InputMealView.this, IngredientsView.class));
//             return true;
//         } else if (id == R.id.ShoppingList) {
//             startActivity(new Intent(InputMealView.this, ShoppingListView.class));
//             return true;
//         }
//     }


/////////TESTING DATA VIZ FUNCTIONALITY


package com.example.greenplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.charts.Cartesian;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.example.greenplate.R;
import com.example.greenplate.viewmodels.InputMealViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class InputMealView extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private EditText editMealText;
    private EditText editCalorieText;
    private Button enterMealButton;
    private EditText editDateText;
    private Button visualizeButton;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Meals");
    private InputMealViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal);
        viewModel = new ViewModelProvider(this).get(InputMealViewModel.class);
        editMealText = findViewById(R.id.InputMealName);
        editCalorieText = findViewById(R.id.InputCalories);
        editDateText = findViewById(R.id.InputDate);
        enterMealButton = findViewById(R.id.InputMealButton);
        visualizeButton = findViewById(R.id.visualizeButton);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        visualizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("InputMealView", "Visualize button clicked"); // Add this line for logging
                viewModel.fetchDailyCaloricIntake();
            }
        });

        // viewModel.getDailyCaloricIntake().observe(this, new Observer<List<Integer>>() {
        //     @Override
        //     public void onChanged(List<Integer> dailyCaloricIntake) {
        //         if (dailyCaloricIntake != null) {
        //             createCaloricIntakeChart(dailyCaloricIntake);
        //         } else {
        //             Toast.makeText(InputMealView.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
        //         }
        //     }
        // });

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

        enterMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String calorieText = editCalorieText.getText().toString();
                String mealName = editMealText.getText().toString();
                String date = editDateText.getText().toString();
                if (mealName.isEmpty()) {
                    Toast.makeText(InputMealView.this, "The 'Meal Name' cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (calorieText.isEmpty()) {
                    Toast.makeText(InputMealView.this, "Calories field cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (date.isEmpty()) {
                    Toast.makeText(InputMealView.this, "Date cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (date.length() != 10) {
                    Toast.makeText(InputMealView.this, "Date is in invalid format", Toast.LENGTH_SHORT).show();
                    Toast.makeText(InputMealView.this, "The 'Calories' field cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int calorieValue = Integer.parseInt(calorieText);
                        DatabaseReference newMealRef = root.push();
                        newMealRef.child("Meal Name").setValue(mealName);
                        newMealRef.child("Date").setValue(date);
                        newMealRef.child("Calories").setValue(calorieValue)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(InputMealView.this, "Meal saved!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(InputMealView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (NumberFormatException e) {
                        Toast.makeText(InputMealView.this, "Invalid Calorie Input", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Home) {
            startActivity(new Intent(InputMealView.this, Home.class));
            return true;
        } else if (id == R.id.Recipe) {
            startActivity(new Intent(InputMealView.this, RecipeView.class));
            return true;
        } else if (id == R.id.InputMeal) {
            return true;
        } else if (id == R.id.Ingredients) {
            startActivity(new Intent(InputMealView.this, IngredientsView.class));
            return true;
        } else if (id == R.id.ShoppingList) {
            startActivity(new Intent(InputMealView.this, ShoppingListView.class));
            return true;
        }
        return false;
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
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        anyChartView.setChart(columnChart);
    }
}

