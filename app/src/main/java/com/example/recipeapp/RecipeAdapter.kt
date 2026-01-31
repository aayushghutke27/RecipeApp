package com.example.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemMyRecipeBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.recipeapp.model.RecipeDataClass

class RecipeAdapter(
    private val onRecipeClick: (RecipeDataClass) -> Unit,
    private val onDeleteClick:(RecipeDataClass) -> Unit,
    private val onEditClick:(RecipeDataClass) -> Unit,
    ) : ListAdapter<RecipeDataClass, RecipeAdapter.RecipeViewHolder>(RecipeDiffCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {
        val binding =
            ItemMyRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecipeViewHolder,
        position: Int
    ) {

        val recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class RecipeViewHolder(private val binding: ItemMyRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeDataClass) {
            binding.tvRecipeTitle.text = recipe.recipeTitle
            binding.tvRecipeAddDescription.text = recipe.recipeDescription

            Glide.with(binding.ivRecipeAddImg.context).load(recipe.recipeImgUrl)
                .into(binding.ivRecipeAddImg)

            binding.root.setOnClickListener {
                onRecipeClick(recipe)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(recipe)
            }

            binding.btnEditRecipe.setOnClickListener {
                onEditClick(recipe)
            }

        }

    }


    class RecipeDiffCallBack : DiffUtil.ItemCallback<RecipeDataClass>() {
        override fun areItemsTheSame(
            oldItem: RecipeDataClass,
            newItem: RecipeDataClass
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecipeDataClass,
            newItem: RecipeDataClass
        ): Boolean {
            return oldItem == newItem
        }

    }

}
