package com.example.recipeapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeDataClass(

    val id: String = "",
    val recipeTitle : String ="",
    val recipeDescription: String = "",
    val recipeCategory: String = "",
    val recipePrepTime: String ="",
    val recipeCookTime: String ="",
    val recipeImgUrl: String="",
    val userId: String="",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()

): Parcelable {
   constructor() : this("", "","","","","","","", 0L, 0L)
}
