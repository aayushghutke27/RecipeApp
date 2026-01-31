package com.example.recipeapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Recipe(
    val name: String,
    val imageUrl: String,
    val cookingDuration: String,
    val description: String,
    val category: String,
    val ingredients: String,
    val directions: String,
    val userId: String,
    val createdAt: Long,
    val updatedAt: Long

): Parcelable
