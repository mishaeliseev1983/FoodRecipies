package com.melyseev.foodrecipes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.melyseev.foodrecipes.adapters.OnRecipeListener;
import com.melyseev.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.melyseev.foodrecipes.models.Recipe;
import com.melyseev.foodrecipes.requests.RecipeApi;
import com.melyseev.foodrecipes.requests.ServiceGenerator;
import com.melyseev.foodrecipes.requests.responses.RecipeResponse;
import com.melyseev.foodrecipes.requests.responses.RecipeSearchResponse;
import com.melyseev.foodrecipes.viewmodels.RecipeListViewModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel recipeListViewModel;
    private RecyclerView recyclerView;
    private RecipeRecyclerAdapter recipeRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);


        recipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        initLayoutAdapter();
        subscribeObservers();
        testRetrofitRequest2();

        //Button button = findViewById(R.id.test);
        /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRetrofitRequest2();
            }
        });
         */

    }

    private void initLayoutAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        recyclerView.setAdapter(recipeRecyclerAdapter);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
    }

    private void subscribeObservers(){
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {

                //recipes.stream().forEach(e->Log.d(TAG, e.getTitle()));

                for(Recipe recipe: recipes){
                    Log.d(TAG, "onChanged: " + recipe.getTitle());
                }
                recipeRecyclerAdapter.setRecipeList(recipes);
            }
        });
    }

    private void testRetrofitRequest2(){
        recipeListViewModel.searchRecipes("chicken", 1);
    }


        private void testRetrofitRequest(){
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

        Call<RecipeSearchResponse> recipeSearchResponseCall= recipeApi
                .searchRecipe("chicken breast",
                                "1"
                );

        recipeSearchResponseCall.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {

                Log.d(TAG, "onResponse: server response: "+ response.toString());
                //if(response.isSuccessful())
                if(response.code() == 200){

                    Log.d(TAG, "onResponse: " + response.body());
                    List<Recipe> recipeList = response.body().getRecipes();
                    for(Recipe recipe: recipeList){
                        Log.d(TAG, "onResponse: " + recipe.getTitle());
                    }
                }else {
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {

            }
        });

    }

    private void test_getRecipe_RetrofitRequest(){
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

        //7fe8bc
        Call<RecipeResponse> recipeResponseCall= recipeApi
                .getRecipe("7fe8bc"
                );

        recipeResponseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {

                Log.d(TAG, "onResponse: server response: "+ response.toString());
                //if(response.isSuccessful())
                if(response.code() == 200){

                    Log.d(TAG, "onResponse: " + response.body());
                    Recipe recipe= response.body().getRecipe();
                    //Log.d(TAG, "recipe title = " + recipe.getTitle() + "; " + recipe.getIngredients().toString());

                    Log.d(TAG, "retrive recipe = " + recipe.toString());

                }else {
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }
}
