package com.example.recipeapp

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_recipes")
data class SavedRecipeEntity(

    @PrimaryKey
    val id: String,
    val recipeTitle : String,
    val recipeDescription: String,
    val recipeCategory: String,
    val recipePrepTime : String,
    val recipeCookTime : String,
    val recipeImageUrl : String,
    val savedAt: Long = System.currentTimeMillis()
)
