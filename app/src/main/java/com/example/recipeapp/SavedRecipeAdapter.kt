package com.example.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.ItemSavedRecipeBinding
import com.example.recipeapp.model.RecipeDataClass

class SavedRecipeAdapter(
    private val onRecipeClick: (RecipeDataClass) -> Unit,
    private val onFavoriteClick: (RecipeDataClass) -> Unit,
    private val isFavorite: (String) -> Boolean
) : ListAdapter<RecipeDataClass, SavedRecipeAdapter.SavedRecipeViewHolder>(RecipeAdapter.RecipeDiffCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedRecipeViewHolder {
        val binding = ItemSavedRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return SavedRecipeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SavedRecipeViewHolder,
        position: Int
    ) {
        val recipe = getItem(position)
        holder.bind(recipe)

    }

    inner class SavedRecipeViewHolder(private val binding: ItemSavedRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeDataClass) {

            binding.tvRecipeName.text = recipe.recipeTitle
            binding.tvRecipeProcess.text = recipe.recipeDescription
            binding.tvCookTime.text = recipe.recipeCookTime

            Glide.with(binding.ivRecipeImg.context).load(recipe.recipeImgUrl).centerCrop()
                .into(binding.ivRecipeImg)

            updateFavoriteIcon(recipe.id)

            binding.root.setOnClickListener {
                onRecipeClick(recipe)
            }

            binding.ivFavorite.setOnClickListener {
                onFavoriteClick(recipe)
                updateFavoriteIcon(recipe.id)
            }

        }

        private fun updateFavoriteIcon(recipeId: String) {
            val saved = isFavorite(recipeId)

            if (saved) {
                binding.ivFavorite.setImageResource(R.drawable.ic_heart_filled)
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_heart_outline)

            }
        }



    }

}
