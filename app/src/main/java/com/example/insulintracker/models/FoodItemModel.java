package com.example.insulintracker.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItemModel implements Parcelable {
    private String title;
    private int calories;
    private String servingSize;
    private int grams;
    private int proteins;
    private int carbs;
    private int fat;
    private int sugar;
    private String docId;

    public FoodItemModel(String title, int calories, int grams, int proteins, int carbs, int fat, String size) {
        this.title = title;
        this.calories = calories;
        this.grams = grams;
        this.carbs = carbs;
        this.fat = fat;
        this.proteins = proteins;
        this.servingSize = size;
    }

    protected FoodItemModel(Parcel in) {
        title = in.readString();
        calories = in.readInt();
        servingSize = in.readString();
        grams = in.readInt();
        proteins = in.readInt();
        carbs = in.readInt();
        fat = in.readInt();
        sugar = in.readInt();
        docId = in.readString();
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public static final Creator<FoodItemModel> CREATOR = new Creator<FoodItemModel>() {
        @Override
        public FoodItemModel createFromParcel(Parcel in) {
            return new FoodItemModel(in);
        }

        @Override
        public FoodItemModel[] newArray(int size) {
            return new FoodItemModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public int getCalories() {
        return calories;
    }

    public String getServingSize() {
        return servingSize;
    }

    public int getGrams() {
        return grams;
    }

    public int getProteins() {
        return proteins;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFat() {
        return fat;
    }

    public int getSugar() {
        return sugar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.calories);
        dest.writeString(this.servingSize);
        dest.writeInt(this.grams);
        dest.writeInt(this.proteins);
        dest.writeInt(this.carbs);
        dest.writeInt(this.fat);
        dest.writeString(docId);
    }
}
