package com.example.recipeapp

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.recipeapp.model.RecipeDataClass

class SavedRecipeRepository(context: Context) {

    private val dao = RecipeDatabase.getDatabase(context).savedRecipeDao()

    fun getAllSavedRecipe(): LiveData<List<SavedRecipeEntity>>{
        return dao.getAllSavedRecipe()
    }

    fun getAllSavedRecipeId(): LiveData<List<String>>{
        return dao.getAllSavedRecipeId()
    }

    fun isRecipeSaved(recipeId: String): LiveData<SavedRecipeEntity?>{
        return dao.getRecipeById(recipeId)
    }

    suspend fun savedRecipe(recipe: RecipeDataClass) {

        val entity = SavedRecipeEntity(
            id = recipe.id,
            recipeTitle = recipe.recipeTitle,
            recipeDescription = recipe.recipeDescription,
            recipeCategory = recipe.recipeCategory,
            recipePrepTime = recipe.recipePrepTime,
            recipeCookTime = recipe.recipeCookTime,
            recipeImageUrl = recipe.recipeImgUrl
        )
        dao.insertRecipe(entity)
    }

    suspend fun unsavedRecipe(recipeId: String){
        dao.deleteRecipeById(recipeId)
    }

    suspend fun toggleSaveStatus(recipe: RecipeDataClass): Boolean{
        val isSaved = dao.isRecipeSaved(recipe.id)

        return if (isSaved){
            dao.deleteRecipeById(recipe.id)
            false
        }else{
            savedRecipe(recipe)
            true
        }

    }
}