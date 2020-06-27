package com.melyseev.foodrecipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.melyseev.foodrecipes.models.Recipe;
import com.melyseev.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private boolean isPerformingQuery;
    private boolean isViewRecipes;
    private RecipeRepository recipeRepository;
    public RecipeListViewModel(){
        recipeRepository= RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipeRepository.getRecipes();
    }
    public void searchRecipes(String query, int pageNumber){
        setPerformingQuery( true );
        recipeRepository.searchRecipes(query, pageNumber);
        setViewRecipes(true);
    }

    public void searchNextPage(){
        recipeRepository.searchNextPage();
    }

    public void cancelRequest(){
        recipeRepository.cancelRequest();
    }

    public boolean isPerformingQuery() {
        return isPerformingQuery;
    }
    public void setPerformingQuery(boolean performingQuery) {
        isPerformingQuery = performingQuery;
    }

    public boolean isViewRecipes() {
        return isViewRecipes;
    }
    public void setViewRecipes(boolean viewRecipes) {
        isViewRecipes = viewRecipes;
    }

    public boolean onBackPressed(){
        if(isPerformingQuery()){
            //cancel query
            cancelRequest();
            setPerformingQuery(false);
        }
        if(isViewRecipes()){
                setViewRecipes( false );
                return false;
        }
        return true;
    }
}
