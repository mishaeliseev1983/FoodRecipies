package com.melyseev.foodrecipes.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.melyseev.foodrecipes.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeSearchResponse {

    @SerializedName("count")
    @Expose()
    private  int count;

    @SerializedName("recipes")
    @Expose
    private List<Recipe> recipes;

    public int getCount() {
        return count;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "count=" + count +
                ", recipes=" + recipes +
                '}';
    }
}
