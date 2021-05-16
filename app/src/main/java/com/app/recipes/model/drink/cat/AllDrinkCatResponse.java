
package com.app.recipes.model.drink.cat;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllDrinkCatResponse {

    @SerializedName("drinks")
    @Expose
    private List<Drink> drinks = null;

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }

}
