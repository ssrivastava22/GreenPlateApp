package com.example.greenplate.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenplate.R;

import java.util.ArrayList;
import java.util.Map;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.MyViewHolder> {

    private ArrayList<ShoppingListModel> shoppingList;
    private OnShoppingClickListener onShoppingClickListener;

    public ShoppingListAdapter(ArrayList<ShoppingListModel> list,
                               OnShoppingClickListener listener) {
        shoppingList = list;
        this.onShoppingClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppinglist_item,
                parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ShoppingListModel item = shoppingList.get(position);
        holder.shoppingListItems.setText(item.getShoppingItemName());
        holder.quantities.setText(String.valueOf(item.getQuantity()));
        holder.shoppingListItems.setChecked(item.isChecked());
        holder.shoppingListItems.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update item's checked status
            item.setChecked(isChecked);
        });
    }


    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CheckBox shoppingListItems;
        private TextView quantities;
        //private CheckBox checkbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            shoppingListItems = itemView.findViewById(R.id.shoppingItemNameCheckBox);
            quantities = itemView.findViewById(R.id.shoppingQuantityTextView);
        }
    }

    public interface OnShoppingClickListener {
        void onShoppingClick(ShoppingListModel shoppingListModel);
    }
}
