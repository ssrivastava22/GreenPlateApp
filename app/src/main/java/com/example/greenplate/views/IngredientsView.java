package com.example.greenplate.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.greenplate.R;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenplate.model.IngredientsModel;
import com.example.greenplate.model.PersonalInfoModel;
import com.example.greenplate.model.User;
import com.example.greenplate.viewmodels.IngredientsViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientsView extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private EditText ingredientNameEditText;
    private EditText quantityEditText;
    private EditText caloriesEditText;
    private EditText expirationDateEditText;
    private Button submitButton;

    private User user = User.getInstance();

    private FirebaseDatabase db;
    private IngredientsViewModel viewModel;

    private DatabaseReference root;
    private RecyclerView recyclerView;
    private IngredientsAdapter adapter;
    private ArrayList<IngredientsModel> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        initializeViews();
        setupRecyclerView();
        fetchIngredients(new IngredientFetchCallback() {
            @Override
            public void onIngredientsFetched(List<IngredientsModel> ingredients) {
                ingredientList.clear();
                ingredientList.addAll(ingredients);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(IngredientsView.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        ingredientNameEditText = findViewById(R.id.ingredient_name_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        caloriesEditText = findViewById(R.id.calories_edit_text);
        expirationDateEditText = findViewById(R.id.expiration_date_edit_text);
        submitButton = findViewById(R.id.submit_button);

        db = FirebaseDatabase.getInstance();
        root = db.getReference().child("Pantry");
        viewModel = new ViewModelProvider(this).get(IngredientsViewModel.class);

        submitButton.setOnClickListener(v -> handleSubmitButtonClick());
    }

    private void handleSubmitButtonClick() {
        String ingredientName = ingredientNameEditText.getText().toString().trim();
        String quantity = quantityEditText.getText().toString().trim();
        String calories = caloriesEditText.getText().toString().trim();
        String expirationDate = expirationDateEditText.getText().toString().trim();

        if (ingredientName.isEmpty() || quantity.isEmpty() || calories.isEmpty()) {
            Toast.makeText(IngredientsView.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double quantityValue = Double.parseDouble(quantity);
            if (quantityValue <= 0) {
                Toast.makeText(IngredientsView.this, "Quantity must be positive.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(IngredientsView.this, "Invalid quantity entered.", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.checkIngredientExists(ingredientName, exists -> {
            if (exists) {
                Toast.makeText(IngredientsView.this, "Ingredient already exists in pantry.", Toast.LENGTH_SHORT).show();
            } else {
                addIngredientToPantry(ingredientName, quantity, calories, expirationDate);
            }
        });
    }


    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.ingredients_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientsAdapter(ingredientList, root);
        recyclerView.setAdapter(adapter);
    }

    public interface IngredientFetchCallback {
        void onIngredientsFetched(List<IngredientsModel> ingredients);
        void onError(String message);
    }

    private void fetchIngredients(IngredientFetchCallback callback) {
        String username = User.getInstance().getUsername();
        if (username != null && !username.isEmpty()) {
            String sanitizedUsername = username.split("@")[0].replaceAll("[.#$\\[\\]]", "");
            DatabaseReference userRef = root.child(sanitizedUsername);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<IngredientsModel> fetchedIngredients = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //IngredientsModel ingredient = snapshot.getValue(IngredientsModel.class);
                        String ingredientStr = snapshot.getKey();
                        String quantityStr;
                        Object quantityObj = snapshot.child("quantity").getValue();
                        if (quantityObj instanceof Long || quantityObj instanceof Integer) {
                            quantityStr = String.valueOf(quantityObj);
                        } else if (quantityObj instanceof String) {
                            quantityStr = (String) quantityObj;
                            try {
                                long quantityLong = Long.parseLong(quantityStr);
                                quantityStr = String.valueOf(quantityLong);
                            } catch (NumberFormatException e) {
                                quantityStr = "Invalid Format";
                            }
                        } else {
                            quantityStr = "Unknown Quantity";
                        }


                        //String quantityStr = snapshot.child("quantity").getValue(String.class);
                        //String quantityStr = "999";
                        //String caloriesStr = "999";
                        //String expirationDateStr = "9/9/9";
                        String caloriesStr = snapshot.child("calories").getValue(String.class);
                        String expirationDateStr = snapshot.child("expirationDate").getValue(String.class);
                        IngredientsModel ingredient = new IngredientsModel(ingredientStr, quantityStr, caloriesStr, expirationDateStr);
                        if (ingredient != null) {
                            fetchedIngredients.add(ingredient);
                        }
                    }
                    callback.onIngredientsFetched(fetchedIngredients);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    callback.onError("Failed to load ingredients.");
                }
            });
        } else {
            callback.onError("Username not set or invalid.");
        }
    }


    public void addIngredientToPantry(String ingredientName, String quantity, String calories, String expirationDate) {
        String username = user.getUsername();
        if (username != null && !username.isEmpty()) {
            String sanitizedUsername = username.split("@")[0].replaceAll("[.#$\\[\\]]", "");

            DatabaseReference userRef = root.child(sanitizedUsername);
            DatabaseReference ingredientRef = userRef.child(ingredientName);

            Map<String, Object> ingredientData = new HashMap<>();
            ingredientData.put("quantity", quantity);
            ingredientData.put("calories", calories);
            ingredientData.put("expirationDate", expirationDate);

            ingredientRef.setValue(ingredientData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(IngredientsView.this,
                                    "Ingredient added to pantry.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(
                                    IngredientsView.this,
                                    IngredientsView.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(IngredientsView.this,
                                    "Failed to add ingredient to pantry: "
                                            + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(IngredientsView.this,
                    "Username not set", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Home) {
            startActivity(new Intent(IngredientsView.this, Home.class));
            return true;
        } else if (id == R.id.Recipe) {
            startActivity(new Intent(IngredientsView.this, RecipeView.class));
            return true;
        } else if (id == R.id.InputMeal) {
            startActivity(new Intent(IngredientsView.this, InputMealView.class));
            return true;
        } else if (id == R.id.Ingredients) {
            return true;
        } else if (id == R.id.ShoppingList) {
            startActivity(new Intent(IngredientsView.this, ShoppingListView.class));
            return true;
        }
        return false;
    }
}
