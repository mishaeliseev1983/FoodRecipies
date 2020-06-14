package com.melyseev.foodrecipes.adapters;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.melyseev.foodrecipes.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "RecipeViewHolder";
    TextView title, publisher, socialScore;
    AppCompatImageView imageView;
    OnRecipeListener onRecipeListener;


    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {

        super(itemView);
        this.onRecipeListener= onRecipeListener;
        title= itemView.findViewById(R.id.recipe_title);
        publisher= itemView.findViewById(R.id.recipe_publisher);
        socialScore = itemView.findViewById(R.id.recipe_social_score);
        imageView = itemView.findViewById(R.id.recipe_image);
        itemView.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: position = " + getAdapterPosition());
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }
}
