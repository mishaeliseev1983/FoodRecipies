package com.melyseev.foodrecipes.requests;

import com.melyseev.foodrecipes.requests.responses.RecipeResponse;
import com.melyseev.foodrecipes.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {


    //SEARCH REQUEST
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("q") String query,
            @Query("page") String page);

    //GET RECIPE REQUEST
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("rId") String recipe_id);
}
