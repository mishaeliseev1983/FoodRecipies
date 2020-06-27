package com.melyseev.foodrecipes.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.melyseev.foodrecipes.R;
import com.melyseev.foodrecipes.models.Recipe;
import com.melyseev.foodrecipes.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LOADING_TYPE = 0;
    private static final int RECIPE_TYPE = 1;
    private static final int CATEGORY_TYPE = 2;

    private List<Recipe> recipeList;
    private OnRecipeListener onRecipeListener;


    public RecipeRecyclerAdapter( OnRecipeListener onRecipeListener1){
        onRecipeListener= onRecipeListener1;
    }

    public void diplayLoading(){
        if(!isLoading()){
            List<Recipe> loadingRecipe= new ArrayList<>();
            Recipe loadRecipe= new Recipe();
            loadRecipe.setTitle("LOAD");
            loadingRecipe.add(loadRecipe);

            recipeList= loadingRecipe;
            notifyDataSetChanged();
        }
    }

    public void displaySearchCategories(){
        List<Recipe> categories = new ArrayList<>();
        for(int i = 0; i< Constants.DEFAULT_SEARCH_CATEGORIES.length; i++){
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        recipeList = categories;
        notifyDataSetChanged();
    }


    private boolean isLoading(){
        if(recipeList==null)
            return false;
        if(recipeList.size()>0 && recipeList.get( recipeList.size()-1).getTitle().equals("LOAD"))
            return true;
        return false;
    }


    @Override
    public int getItemViewType(int position) {
        if(isLoading())
            return  LOADING_TYPE;
        else if(recipeList.get(position).getSocial_rank() == -1) {
            return CATEGORY_TYPE;
        }
        return RECIPE_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //int itemViewType= getItemViewType(viewType);
        View view;
        switch (viewType){
            case CATEGORY_TYPE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, onRecipeListener);
            }
            case LOADING_TYPE:{
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            }
            default:{
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, onRecipeListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == RECIPE_TYPE) {
            RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
            Recipe currentRecipe = recipeList.get(position);


            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(currentRecipe.getImage_url())
                    .into(recipeViewHolder.imageView);

            recipeViewHolder.title.setText(currentRecipe.getTitle());
            recipeViewHolder.publisher.setText(currentRecipe.getPublisher());
            recipeViewHolder.socialScore.setText(String.valueOf(Math.round(currentRecipe.getSocial_rank())));
        }
        if(itemViewType == CATEGORY_TYPE){
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);
            Recipe currentRecipe = recipeList.get(position);
            Uri path = Uri.parse("android.resource://com.melyseev.foodrecipes/drawable/" + currentRecipe.getImage_url());

            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(path)
                    .into(categoryViewHolder.categoryImage);
            categoryViewHolder.categoryTitle.setText(currentRecipe.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        if(recipeList==null)
            return 0;
        return recipeList.size();
    }

    public void setRecipeList(List<Recipe> list){
        recipeList= list;
        notifyDataSetChanged();
    }
}
