package com.example.recipeapp

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database([SavedRecipeEntity::class], version = 1, exportSchema = false)

abstract class RecipeDatabase : RoomDatabase() {

    abstract fun savedRecipeDao(): SavedRecipeDao

    companion object {
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java, "recipe_database"
                ).build()

                INSTANCE = instance
                instance
            }


        }
    }
}