package com.melyseev.foodrecipes.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.melyseev.foodrecipes.models.Recipe;
import com.melyseev.foodrecipes.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;
    private String query;
    private int pageNumber;

    public static RecipeRepository getInstance(){
        if (instance==null){
            instance= new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository(){
        recipeApiClient= RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeApiClient.getRecipes();
    }

    public void searchRecipes(String query, int pageNumber){
        if(pageNumber==0)
            pageNumber=1;
        this.query= query;
        this.pageNumber= pageNumber;
        recipeApiClient.searchRecipes(query, pageNumber);
    }

    public void searchNextPage(){
        searchRecipes(query, pageNumber+1);
    }
    public void cancelRequest(){ recipeApiClient.cancelRequest();}
}
