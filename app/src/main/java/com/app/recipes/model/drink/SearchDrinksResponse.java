package com.app.recipes.model.drink;

import com.app.recipes.model.drink.drink.Drink;
import com.app.recipes.model.food.meals.details.Meal;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchDrinksResponse {
    @SerializedName("drinks")
    @Expose
    List<Drink> mealList=null;

    public List<Drink> getMealList() {
        return mealList;
    }

    public void setMealList(List<Drink> mealList) {
        this.mealList = mealList;
    }
}
