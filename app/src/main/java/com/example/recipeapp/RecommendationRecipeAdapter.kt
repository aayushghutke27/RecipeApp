package com.example.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.recipeapp.databinding.ItemRecipeCardBinding


class RecommendationRecipeAdapter(
    private var recipes: List<Recipe>,
    private val onRecipeClick: (Recipe) -> Unit,
    private val onFavoriteClick: ((Recipe) -> Unit)? = null,
    private val isFavorite: ((String) -> Boolean)? = null
) :
    RecyclerView.Adapter<RecommendationRecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {
        val binding =
            ItemRecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecipeViewHolder,
        position: Int
    ) {
        holder.bind(recipes[position])
    }


    override fun getItemCount(): Int {
        return recipes.size
    }

    fun updateList(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(val binding: ItemRecipeCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            Glide.with(binding.root.context)
                .load(recipe.imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivRecipeImg)

            binding.tvCookTime.text = recipe.cookingDuration
            binding.tvRecipeName.text = recipe.name
            binding.tvRecipeProcess.text = recipe.description

            if (onFavoriteClick != null && isFavorite != null) {

                updateFavoriteIcon(recipe)

                binding.ivFavorite.setOnClickListener {
                    onFavoriteClick.invoke(recipe)
                    updateFavoriteIcon(recipe)
                }

            } else {
                binding.ivFavorite.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onRecipeClick(recipe)
            }

        }

        private fun updateFavoriteIcon(recipe: Recipe) {

            val saved = isFavorite?.invoke(recipe.name) ?: false

            if (saved) {
                binding.ivFavorite.setImageResource(R.drawable.ic_heart_filled)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_heart_outline)
            }

        }


    }
}