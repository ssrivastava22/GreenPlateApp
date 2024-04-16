package com.example.greenplate.model;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenplate.R;

import java.util.ArrayList;
import java.util.Map;

public class ShoppingListModel {
    private String shoppingItemName;
    private int quantity;
    private boolean isChecked;

    public ShoppingListModel() {

    }
    public ShoppingListModel(String shoppingItemName, int quantity, boolean isChecked) {
        this.shoppingItemName = shoppingItemName;
        this.quantity = quantity;
        this.isChecked = isChecked;
    }

    public ShoppingListModel(String ingredientStr, String quantityStr, String caloriesStr, String expirationDateStr) {
    }

    public String getShoppingItemName() {
        return shoppingItemName;
    }

    public void setShoppingItemName(String shoppingItemName) {
        this.shoppingItemName = shoppingItemName;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
