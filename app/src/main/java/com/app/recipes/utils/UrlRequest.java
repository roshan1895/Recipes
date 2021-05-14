package com.app.recipes.utils;

import com.app.recipes.model.categories.AllCategoryResponse;
import com.app.recipes.model.meals.AllMealsResponse;
import com.app.recipes.model.meals.details.MealDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface UrlRequest {

    @GET("v1/1/filter.php")
    Call<AllMealsResponse>allMeal(@Query("c") String category);
    @GET("v1/1/categories.php")
    Call<AllCategoryResponse> allCategory();
    @GET("v1/1/lookup.php")
    Call<MealDetailsResponse> mealDetails(@Query("i")String id);




}
