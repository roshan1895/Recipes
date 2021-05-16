package com.app.recipes.model.food;

import com.app.recipes.model.food.meals.Meal;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchFoodResponse {
    @SerializedName("meals")
    @Expose
    List<Meal> mealList=null;

    public List<Meal> getMealList() {
        return mealList;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
    }
}
