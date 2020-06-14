package com.melyseev.foodrecipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.melyseev.foodrecipes.models.Recipe;
import com.melyseev.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    public RecipeListViewModel(){
        recipeRepository= RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipeRepository.getRecipes();
    }
    public void searchRecipes(String query, int pageNumber){
        recipeRepository.searchRecipes(query, pageNumber);
    }
}
