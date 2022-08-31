package com.example.insulintracker.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.insulintracker.models.FoodItemModel;
import com.example.insulintracker.adapters.FoodLVAdapter;
import com.example.insulintracker.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFoodActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageButton searchBtn;
    private ImageButton barcodeBtn;
    private EditText searchInput;
    private ListView foodsList;
    private OkHttpClient http;
    private ArrayList<FoodItemModel> foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        backBtn = findViewById(R.id.backButton);
        searchBtn = findViewById(R.id.searchButton);
        barcodeBtn = findViewById(R.id.barcodeButton);
        searchInput = findViewById(R.id.search_input);
        foodsList = findViewById(R.id.list_view);
        foods = new ArrayList<FoodItemModel>();
        http = new OkHttpClient();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://trackapi.nutritionix.com/v2/search/instant?query="+searchInput.getText().toString()+"&detailed=true";
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("x-app-id","1a3c48ad")
                        .addHeader("x-app-key","c118a5c43da4cd9793a4a526b207a0af")
                        .build();
                http.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String result = response.body().string();
                        foods.clear();
                        try {
                            JSONObject json = new JSONObject(result);
                            Log.v("json",result);
                            JSONArray commonFood = json.getJSONArray("common");
                            extractAllFood(commonFood);
                            JSONArray brandedFood = json.getJSONArray("branded");
                            extractAllFood(brandedFood);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SearchFoodActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FoodLVAdapter adapter = new FoodLVAdapter(SearchFoodActivity.this,foods.toArray(new FoodItemModel[0]));
                                foodsList.setAdapter(adapter);
                            }
                        });
                    }
                });
            }
        });
        barcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();
            }
        });
    }
    private void extractAllFood(JSONArray foodArray) throws JSONException {
        for(int i =0; i<foodArray.length();i++){
            JSONObject food = foodArray.getJSONObject(i);
            foods.add(extractFood(food));
            }
    }
    private FoodItemModel extractFood(JSONObject food) throws JSONException {
        String foodName = food.getString("food_name");
        String size = food.getString("serving_unit");
        int servingGrams = food.getInt("serving_weight_grams");
        JSONArray nutrients = food.getJSONArray("full_nutrients");
        double calories = 0;
        double carbs = 0;
        double protein =0;
        double fat = 0;
        for(int j = 0; j<nutrients.length();j++){
            JSONObject nutrient = nutrients.getJSONObject(j);
            if(nutrient.getInt("attr_id") == 208)
                calories = nutrient.getDouble("value");
            if(nutrient.getInt("attr_id") == 205)
                carbs = nutrient.getDouble("value");
            if(nutrient.getInt("attr_id") == 203)
                protein = nutrient.getDouble("value");
            if(nutrient.getInt("attr_id") == 204)
                fat = nutrient.getDouble("value");
        }
        return new FoodItemModel(foodName,(int) calories,servingGrams,(int)protein,(int) carbs,(int) fat,size);
    }
    private void scanBarcode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume Up to turn flash on")
                .setBeepEnabled(false)
                .setOrientationLocked(true)
                .setCaptureActivity(BarcodeActivity.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            String url = "https://trackapi.nutritionix.com/v2/search/item?upc="+result.getContents();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("x-app-id","1a3c48ad")
                    .addHeader("x-app-key","c118a5c43da4cd9793a4a526b207a0af")
                    .build();
            http.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body().string();
                    try {
                        JSONObject json = new JSONObject(res);
                        Log.v("json",json.toString());
                        JSONObject foodJSON = json.getJSONArray("foods").getJSONObject(0);
                        Intent intent = new Intent(SearchFoodActivity.this,AddFoodActivity.class);
                        intent.putExtra("food", (Parcelable) extractFood(foodJSON));
                        SearchFoodActivity.this.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    });
}