package com.example.insulintracker.models;

public class MealItemModel {
    private String id;
    private String name;
    private int cals;

    public MealItemModel(String id, String name,int cals) {
        this.id = id;
        this.name = name;
        this.cals = cals;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCals() {
        return cals;
    }
}
