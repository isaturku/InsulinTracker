package com.example.insulintracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.insulintracker.utilities.Auth;
import com.example.insulintracker.utilities.DB;
import com.example.insulintracker.models.FoodItemModel;
import com.example.insulintracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddFoodActivity extends AppCompatActivity {
    TextView foodNameTxt;
    TextView calorieTxt;
    TextView carbTxt;
    TextView proteinTxt;
    TextView fatTxt;
    EditText amountEditTxt;
    Spinner unitSpinner;
    FloatingActionButton fab;
    int cals;
    int carbs;
    int protein;
    int fat;
    int grams;
    String servingSize;
    boolean isUnitGram;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        final int vals[] = new int[] {0,0,0,0};
        isUnitGram = false;
        foodNameTxt = findViewById(R.id.add_food_title);
        unitSpinner =findViewById(R.id.unitSpinner);
        calorieTxt = findViewById(R.id.calorieVal);
        carbTxt = findViewById(R.id.carbVal);
        proteinTxt = findViewById(R.id.proteinVal);
        fatTxt = findViewById(R.id.fatVal);
        amountEditTxt = findViewById(R.id.amountEditTxt);
        fab = findViewById(R.id.uploadFab);
        Intent intent = new Intent(this,MainActivity.class);
        amountEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                double amt = Double.parseDouble(v.getText().toString());
                if(isUnitGram){
                    vals[0] = (int) ((cals*amt)/grams);
                    vals[1] = (int) ((carbs*amt)/grams);
                    vals[2] = (int) ((protein*amt)/grams);
                    vals[3] = (int) ((fat*amt)/grams);
                    updateVals(vals[0],vals[1],vals[2],vals[3]);
                }else{
                    vals[0] = (int) (cals*amt);
                    vals[1] = (int)(carbs*amt);
                    vals[2] = (int)(protein*amt);
                    vals[3] = (int) (fat*amt);
                    updateVals(vals[0],vals[1],vals[2],vals[3]);
                }
                return false;
            }
        });
        FoodItemModel food = (FoodItemModel)getIntent().getParcelableExtra("food");
        cals = food.getCalories();
        vals[0] = cals;
        carbs = food.getCarbs();
        vals[1] = carbs;
        protein = food.getProteins();
        vals[2] = protein;
        fat = food.getFat();
        vals[3] = fat;
        grams = food.getGrams();
        servingSize = food.getServingSize();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,new String[]{servingSize,"grams"});
        unitSpinner.setAdapter(adapter);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int i = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(i != 0) {
                    double currentAmount = Double.parseDouble(amountEditTxt.getText().toString());
                    if (position == 0) {
                        isUnitGram = false;
                        currentAmount /= grams;
                        amountEditTxt.setText("" + currentAmount);
                    } else {
                        isUnitGram = true;
                        currentAmount *= grams;
                        amountEditTxt.setText("" + currentAmount);
                    }
                }
                i++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        foodNameTxt.setText(food.getTitle());
        updateVals(cals,carbs,protein,fat);
        amountEditTxt.setText(""+1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cals = vals[0];
                int carbs = vals[1];
                int protein = vals[2];
                int fat = vals[3];
                DB.insertData(food.getTitle(), Auth.getUser().getUid(), cals,carbs,protein,fat,grams,servingSize);
                startActivity(intent);
            }
        });
    }
    private void updateVals(int cals, int carbs, int protein, int fat){
        calorieTxt.setText(""+cals+"kcal");
        carbTxt.setText(""+carbs+"g");
        proteinTxt.setText(""+protein+"g");
        fatTxt.setText(""+fat+"g");
    }
}