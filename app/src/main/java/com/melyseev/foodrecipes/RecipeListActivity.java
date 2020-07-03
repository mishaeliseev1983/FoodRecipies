package com.melyseev.foodrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.melyseev.foodrecipes.adapters.OnRecipeListener;
import com.melyseev.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.melyseev.foodrecipes.models.Recipe;
import com.melyseev.foodrecipes.util.VerticalSpacingItemDecorator;
import com.melyseev.foodrecipes.viewmodels.RecipeListViewModel;
import java.util.List;


public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel recipeListViewModel;
    private RecyclerView recyclerView;
    private RecipeRecyclerAdapter recipeRecyclerAdapter;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);


        recipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);
        searchView= findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recyclerView);

        initLayoutAdapter();
        subscribeObservers();
        initSearchView();
        recipeRecyclerAdapter.displaySearchCategories();
        recipeListViewModel.setViewRecipes( false );

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recipeRecyclerAdapter.diplayLoading();
                recipeListViewModel.searchRecipes(query, 1);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void initLayoutAdapter() {
        recipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        recyclerView.setAdapter(recipeRecyclerAdapter);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        VerticalSpacingItemDecorator verticalSpacingItemDecorator= new VerticalSpacingItemDecorator(10);
        recyclerView.addItemDecoration( verticalSpacingItemDecorator );
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(recyclerView.canScrollVertically(1) == false){
                    //search the next page
                    Log.e(TAG, "I reached bottom");
                    recipeListViewModel.searchNextPage();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void subscribeObservers(){
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes!=null) {
                    if (recipeListViewModel.isViewRecipes()) {
                        recipeListViewModel.setPerformingQuery(false);
                        recipeRecyclerAdapter.setRecipeList(recipes);
                        Log.e(TAG, "size new recipes: " + recipes.size());
                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
       Log.d(TAG, "onBackPressed");
       if(recipeListViewModel.onBackPressed()){
           super.onBackPressed();
       }else
           recipeRecyclerAdapter.displaySearchCategories();
    }



    private void testRetrofitRequest2(){
        recipeListViewModel.searchRecipes("chicken", 1);
    }

/*
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
     */

    @Override
    public void onRecipeClick(int position) {
          Recipe recipe= recipeRecyclerAdapter.getRecipeByPosition(position);
          Intent intentRecipe= new Intent(this, RecipeActivity.class);
          intentRecipe.putExtra("recipe", recipe);
          startActivity( intentRecipe );
    }

    @Override
    public void onCategoryClick(String category) {
        recipeRecyclerAdapter.diplayLoading();
        recipeListViewModel.searchRecipes( category, 1);
        searchView.clearFocus();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_categories){
            recipeRecyclerAdapter.displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
