package com.melyseev.foodrecipes.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.melyseev.foodrecipes.AppExecutors;
import com.melyseev.foodrecipes.models.Recipe;
import com.melyseev.foodrecipes.requests.responses.RecipeSearchResponse;
import com.melyseev.foodrecipes.util.Constants;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class RecipeApiClient {
    private final String TAG = "RecipeApiClient";
    private static RecipeApiClient instance;

    private MutableLiveData<List<Recipe>> recipes;
    RetrieveSearchRecipesRunnable retrieveSearchRecipesRunnable;

    private RecipeApiClient() {
        recipes = new MutableLiveData<>();
    }

    public static RecipeApiClient getInstance(){
        if(instance==null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipes;
    }


    public void searchRecipes(String query, int pageNumber) {
        ScheduledExecutorService scheduledExecutorService = AppExecutors.getInstance().getScheduledExecutorService();

        if (retrieveSearchRecipesRunnable != null)
            retrieveSearchRecipesRunnable = null;
        retrieveSearchRecipesRunnable = new RetrieveSearchRecipesRunnable(query, pageNumber);


        final Future handler = scheduledExecutorService.submit(retrieveSearchRecipesRunnable);
/*        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {

                //let the user
                handler.cancel( true );
            }
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
*/
    }


    public void cancelRequest(){
        if(retrieveSearchRecipesRunnable!=null){
            retrieveSearchRecipesRunnable.cancelRequest();
        }
        else {
            Log.e(TAG, " retrieveSearchRecipesRunnable is null");
        }
    }


    class RetrieveSearchRecipesRunnable implements Runnable{

        private final String TAG = "RetrieveSearchRecipes";
        String query;
        int pageNumber;
        boolean cancelRequest;

        RetrieveSearchRecipesRunnable(String query, int pageNumber){
            this.query= query;
            this.pageNumber= pageNumber;
            cancelRequest= false;
        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber){
            return  ServiceGenerator.getRecipeApi().searchRecipe(query, String.valueOf(pageNumber));
        }

        @Override
        public void run() {
            try {
                Response response= getRecipes(query, pageNumber).execute();

                if(cancelRequest==true)
                    return;

                if(response.isSuccessful()){
                    RecipeSearchResponse recipeSearchResponse = (RecipeSearchResponse) response.body();
                    List<Recipe> recipesFromResponse =  recipeSearchResponse.getRecipes();
                    if(pageNumber==1){
                        recipes.postValue(recipesFromResponse);
                    }
                    else{
                        String errorBody = response.errorBody().toString();


                        Log.e(TAG, "run: "+ errorBody);
                        recipes.getValue().addAll(recipesFromResponse);
                    }


                }else{

                     recipes.postValue( null );
                }
            } catch (IOException e) {
                e.printStackTrace();
                recipes.postValue( null );
            }


        }

        public void cancelRequest() {
            Log.e(TAG, " cancel request");
            cancelRequest= true;
        }
    }
}
