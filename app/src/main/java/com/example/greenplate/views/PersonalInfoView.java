package com.example.greenplate.views;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.greenplate.R;
import com.example.greenplate.model.PersonalInfoModel;
import com.example.greenplate.viewmodels.PersonalInfoViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.greenplate.viewmodels.InputMealViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import android.content.Intent;

public class PersonalInfoView extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private EditText editHeightText;
    private EditText editWeightText;

    private EditText editGenderText;
    private Button enterUserInfoButton;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");
    private PersonalInfoModel viewModel;

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
                    Toast.makeText(PersonalInfoView.this, "The 'Height' cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (weightText.isEmpty()) {
                    Toast.makeText(PersonalInfoView.this, "The 'Weight' cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (genderText.isEmpty()) {
                    Toast.makeText(PersonalInfoView.this, "The 'Gender' cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double height = Double.parseDouble(heightText);
                        double weight = Double.parseDouble(weightText);

                        if (height <= 0 || weight <= 0) {
                            Toast.makeText(PersonalInfoView.this, "Height and weight must be greater than zero", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference newUserRef = root.push();
                            newUserRef.child("Height").setValue(height);
                            newUserRef.child("Weight").setValue(weight);
                            newUserRef.child("Gender").setValue(genderText);

                            Toast.makeText(PersonalInfoView.this, "Personal information saved!", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(PersonalInfoView.this, "Personal information saved!", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(PersonalInfoView.this, "Invalid input", Toast.LENGTH_SHORT).show();
                    }

                    // will need to be updated once personal info screen gets created
                    @Override
                    public boolean onNavigationItemSelected (@NonNull MenuItem item){
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
                    }
                }
            }
        });
    }
}
