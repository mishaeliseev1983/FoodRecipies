package com.melyseev.foodrecipes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.melyseev.foodrecipes.R;
import com.melyseev.foodrecipes.models.Recipe;

import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> recipeList;
    private OnRecipeListener onRecipeListener;


    public RecipeRecyclerAdapter( OnRecipeListener onRecipeListener1){
        onRecipeListener= onRecipeListener1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
        return new RecipeViewHolder(view, onRecipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        RecipeViewHolder recipeViewHolder= (RecipeViewHolder) holder;
        Recipe currentRecipe= recipeList.get(position);

        RequestOptions requestOptions= new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions( requestOptions )
                .load(currentRecipe.getImage_url())
                .into(recipeViewHolder.imageView);

        recipeViewHolder.title.setText( currentRecipe.getTitle() );
        recipeViewHolder.publisher.setText( currentRecipe.getPublisher() );
        recipeViewHolder.socialScore.setText( String.valueOf(Math.round( currentRecipe.getSocial_rank()) ));
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
