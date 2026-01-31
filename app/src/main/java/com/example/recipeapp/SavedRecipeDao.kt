package com.example.recipeapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedRecipeDao {

    @Query(" SELECT * FROM saved_recipes ORDER BY savedAt DESC")
    fun getAllSavedRecipe(): LiveData<List<SavedRecipeEntity>>

    @Query("SELECT id FROM saved_recipes")
    fun getAllSavedRecipeId(): LiveData<List<String>>

    @Query("SELECT * FROM saved_recipes WHERE id = :recipeId")
    fun getRecipeById(recipeId: String): LiveData<SavedRecipeEntity?>

    @Query("SELECT EXISTS(SELECT * FROM saved_recipes WHERE id = :recipeId)")
    suspend fun isRecipeSaved(recipeId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: SavedRecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: SavedRecipeEntity)

    @Query("DELETE FROM saved_recipes WHERE id=:recipeId")
    suspend fun deleteRecipeById(recipeId: String)

    @Query("SELECT COUNT(*) FROM saved_recipes")
    fun getSavedRecipeCount() : LiveData<Int>
}