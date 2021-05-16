package com.app.recipes.utils;


import android.app.SearchManager;

import com.app.recipes.model.drink.SearchDrinksResponse;
import com.app.recipes.model.drink.cat.AllDrinkCatResponse;
import com.app.recipes.model.drink.drink.AllDrinkResponse;
import com.app.recipes.model.drink.drink.details.DrinkDetailsResponse;
import com.app.recipes.model.food.SearchFoodResponse;
import com.app.recipes.model.food.cat.AllCategoryResponse;
import com.app.recipes.model.food.meals.AllMealsResponse;
import com.app.recipes.model.food.meals.details.MealDetailsResponse;
import com.app.recipes.ui.drink.DrinkDetailsActivity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UrlRequest {

    @GET("v1/1/filter.php")
    Call<AllMealsResponse>allMeal(@Query("c") String category);
    @GET("v1/1/categories.php")
    Call<AllCategoryResponse> allCategory();
    @GET("v1/1/lookup.php")
    Call<MealDetailsResponse> mealDetails(@Query("i")String id);
    @GET("v1/1/list.php")
    Call<AllDrinkCatResponse> allDrinkCategory(@Query("c")String category);
    @GET("v1/1/filter.php")
    Call<AllDrinkResponse> allDrink(@Query("c")String category);
    @GET("v1/1/lookup.php")
    Call<DrinkDetailsResponse> drinkDetails(@Query("i")String id);
    @GET("v1/1/search.php")
    Call<SearchFoodResponse> searchMeals(@Query("s")String terms);
    @GET("v1/1/search.php")
    Call<SearchDrinksResponse> searchDrinks(@Query("s")String terms);

}
