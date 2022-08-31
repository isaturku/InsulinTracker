package com.example.insulintracker.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.insulintracker.R;
import com.example.insulintracker.models.MealItemModel;
import com.example.insulintracker.utilities.DB;

public class MealLVAdapter extends ArrayAdapter<MealItemModel> {
    private final Activity mContext;
    private final MealItemModel[] meals;

    public MealLVAdapter(Activity context, MealItemModel[] meals) {
        super(context, R.layout.meal_item,meals);
        this.mContext = context;
        this.meals = meals;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View v = inflater.inflate(R.layout.meal_item,null,true);
        MealItemModel item = meals[position];
            TextView mealTxt = v.findViewById(R.id.meal_name);
            TextView calTxt = v.findViewById(R.id.mealCals);
            calTxt.setText(item.getCals()+"kcal");
            mealTxt.setText(item.getName());
            ImageButton deleteBtn = v.findViewById(R.id.deleteMeal);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DB.deleteMeal(item.getId());
                }
            });
        return v;
    }
}
