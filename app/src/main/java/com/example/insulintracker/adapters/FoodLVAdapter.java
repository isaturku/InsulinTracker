package com.example.insulintracker.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.insulintracker.R;
import com.example.insulintracker.activities.AddFoodActivity;
import com.example.insulintracker.models.FoodItemModel;

public class FoodLVAdapter extends ArrayAdapter<FoodItemModel>  {
    private final Activity context;
    private final FoodItemModel[] food;


    public FoodLVAdapter(Activity context, FoodItemModel[] food) {
        super(context, R.layout.food_item,food);
        this.context = context;
        this.food = food;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.food_item,null,true);
        TextView calTxt = rowView.findViewById(R.id.calories);
        TextView foodNameTxt = rowView.findViewById(R.id.food_name);
        foodNameTxt.setText(food[position].getTitle());
        calTxt.setText(""+food[position].getCalories());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddFoodActivity.class);
                intent.putExtra("food", (Parcelable) food[position]);
                context.startActivity(intent);
            }
        });
        return rowView;
    }
}
