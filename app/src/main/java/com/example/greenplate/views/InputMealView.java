package com.example.greenplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.R;
import androidx.lifecycle.ViewModelProvider;

import com.example.greenplate.model.User;
import com.example.greenplate.viewmodels.AccountCreationViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.widget.TextView;
import com.example.greenplate.viewmodels.InputMealViewModel;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InputMealView extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private InputMealViewModel viewModel;
    private TextView userInfoTextView;
    private TextView calorieGoalTextView;
    private TextView dailyCalorieIntakeTextView;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        viewModel = new ViewModelProvider(this).get(InputMealViewModel.class);
        userInfoTextView = findViewById(R.id.userInfoTextView);
        calorieGoalTextView = findViewById(R.id.calorieGoalTextView);
        dailyCalorieIntakeTextView = findViewById(R.id.dailyCalorieIntakeTextView);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        fetchUserData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void fetchUserData() {
        DatabaseReference userReference = mDatabase.child("users").child(username).get();

        String userData = viewModel.getUserInfo(userReference);
        userInfoTextView.setText(userData);
        int totalCalories = viewModel.getCalories(userReference);
        int calorieGoal = viewModel.getCalorieGoal();
        dailyCalorieIntakeTextView.setText("Daily Calorie Intake: " + totalCalories);
        calorieGoalTextView.setText("Calorie Goal: " + calorieGoal);
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
}