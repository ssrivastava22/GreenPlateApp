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
import androidx.lifecycle.ViewModelProvider;

import com.example.greenplate.R;
import com.example.greenplate.viewmodels.PersonalInfoViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalInfoView extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private EditText editHeightText;

    private EditText editWeightText;
    private EditText editGenderText;
    private Button enterUserInfoButton;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("users");
    private PersonalInfoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        viewModel = new ViewModelProvider(this).get(PersonalInfoViewModel.class);
        editHeightText = findViewById(R.id.editHeightText);
        editWeightText = findViewById(R.id.editWeightText);
        editGenderText = findViewById(R.id.editGenderText);
        enterUserInfoButton = findViewById(R.id.enterUserInfoButton);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        enterUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String heightText = editHeightText.getText().toString();
                String weightText = editWeightText.getText().toString();
                String genderText = editGenderText.getText().toString();
                if (heightText.isEmpty()) {
                    Toast.makeText(PersonalInfoView.this, "The 'Height' field cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (weightText.isEmpty()) {
                    Toast.makeText(PersonalInfoView.this, "The 'Weight' field cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (genderText.isEmpty()) {
                    Toast.makeText(PersonalInfoView.this, "The 'Gender' field cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    /*try {
                        DatabaseReference newUserRef = root.push();
                        newUserRef.child("Height").setValue(heightText);
                        newUserRef.child("Weight").setValue(weightText);
                        newUserRef.child("Gender").setValue(genderText)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(PersonalInfoView.this, "User info saved!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PersonalInfoView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (NumberFormatException e) {
                        Toast.makeText(PersonalInfoView.this, "Invalid Gender Input", Toast.LENGTH_SHORT).show();
                    }*/
                    // Save user information to the database
                    viewModel.setHeight(heightText);
                    viewModel.setWeight(weightText);
                    viewModel.setGender(genderText);
                    viewModel.saveUserInfo();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Home) {
            startActivity(new Intent(PersonalInfoView.this, Home.class));
            return true;
        } else if (id == R.id.Recipe) {
            startActivity(new Intent(PersonalInfoView.this, RecipeView.class));
            return true;
        } else if (id == R.id.InputMeal) {
            startActivity(new Intent(PersonalInfoView.this, InputMealView.class));
            return true;
        } else if (id == R.id.Ingredients) {
            startActivity(new Intent(PersonalInfoView.this, IngredientsView.class));
            return true;
        } else if (id == R.id.ShoppingList) {
            startActivity(new Intent(PersonalInfoView.this, ShoppingListView.class));
            return true;
        } else if (id == R.id.PersonalInfo) {
            //startActivity(new Intent(PersonalInfoView.this, PersonalInfoView.class));
            return true;
        }
        return false;
    }
}
