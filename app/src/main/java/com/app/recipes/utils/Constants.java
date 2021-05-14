package com.app.recipes.utils;

public class Constants {
    public static  String FOOD_BASE_URL="https://www.themealdb.com/api/json/";
    public static String INGREDIENTS_IMAGE_URL="www.themealdb.com/images/ingredients/";
    public static String getIngredientsLargeImage(String name)
    {
        return INGREDIENTS_IMAGE_URL+name+".png";
    }
    public static String getIngredientsSmallImage(String name)
    {
        return INGREDIENTS_IMAGE_URL+name+"-Small.png";
    }
}
