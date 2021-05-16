package com.app.recipes.utils;

public class Constants {
    public static  String FOOD_BASE_URL="https://www.themealdb.com/api/json/";
    public  static String DRINK_BASE_URL="https://www.thecocktaildb.com/api/json/";
    public static String FOOD_INGREDIENTS_IMAGE_URL="https://www.themealdb.com/images/ingredients/";
    public static String DRINK_INGREDIENTS_IMAGE_URL="https://www.thecocktaildb.com/images/ingredients/";

    public static String getIngredientsLargeImage(String name)
    {
        return FOOD_INGREDIENTS_IMAGE_URL+name+".png";
    }
    public static String getFoodIngredientsSmallImage(String name)
    {
        return FOOD_INGREDIENTS_IMAGE_URL+name+"-Small.png";
    }
    public static String getDrinkIngredientsSmallImage(String name)
    {
        return DRINK_INGREDIENTS_IMAGE_URL+name+"-Small.png";
    }

}
