package com.app.recipes.model;

import com.airbnb.lottie.model.content.ShapeTrimPath;

public class Ingredient {
    String name;
    String image;
    String qty;
    public Ingredient(String name, String image, String qty)
    {
        this.name=name;
        this.image=image;
        this.qty=qty;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
