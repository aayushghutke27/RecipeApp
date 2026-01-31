package com.example.recipeapp

import com.example.recipeapp.model.RecipeDataClass

fun Recipe.toRecipeDataClass(): RecipeDataClass{

    return RecipeDataClass(
        id = this.name,
        recipeTitle = this.name,
        recipeDescription = this.description,
        recipeCategory = this.category,
        recipePrepTime = "",
        recipeCookTime = this.cookingDuration,
        recipeImgUrl = this.imageUrl,
        userId = this.userId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt

    )

}