package com.example.recipeapp.repository

import android.util.Log
import com.example.recipeapp.model.RecipeDataClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await


class FirebaseRepository {


    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val recipeCollection = firestore.collection("Recipe")

    companion object {
        private const val TAG = "FirestoreRepository"
    }


    private suspend fun getCurrentUserId(): String {

        var user = auth.currentUser

        if (user == null) {

            auth.signInAnonymously().await()
            user = auth.currentUser

        }

        return user?.uid ?: "anonymous"
    }

    suspend fun addRecipe(recipe: RecipeDataClass): Result<String> {

        return try {

            val docRef = recipeCollection.document()
            val userId = getCurrentUserId()

            val recipeWithMetadata = recipe.copy(
                id = docRef.id,
                userId = userId,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

//            val docRef = recipeCollection.add(recipeWithMetadata).await()
//
//            recipeCollection.document(docRef.id).update("id", docRef.id).await()

            docRef.set(recipeWithMetadata).await()

            Result.success(docRef.id)

        } catch (e: Exception) {

            Result.failure(e)
        }

    }

    suspend fun updateRecipe(recipe: RecipeDataClass): Result<Unit> {

        return try {

            val updatedRecipe = recipe.copy(
                updatedAt = System.currentTimeMillis()
            )

            recipeCollection.document(recipe.id).set(updatedRecipe).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return try {

            recipeCollection.document(recipeId).delete().await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserRecipe(): Result<List<RecipeDataClass>> {
        return try {
            val userId = getCurrentUserId()
            Log.d(TAG, "getUserRecipe: Fetching recipes for userId: $userId")

            val snapShot = recipeCollection
                .whereEqualTo("userId", userId)
//                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            Log.d(TAG, "getUserRecipe: Query returned ${snapShot.size()} documents")

            snapShot.documents.forEachIndexed { index, doc ->
                Log.d(TAG, "getUserRecipe: Document $index - ID: ${doc.id}, Data: ${doc.data}")
            }

            val list = snapShot.toObjects(RecipeDataClass::class.java)
            Log.d(TAG, "getUserRecipe: Converted to ${list.size} RecipeDataClass objects")

            list.forEachIndexed { index, recipe ->
                Log.d(TAG, "getUserRecipe: Recipe $index - Title: ${recipe.recipeTitle}, ID: ${recipe.id}, userId: ${recipe.userId}")
            }

            Result.success(list)
        } catch (e: Exception) {
            Log.e(TAG, "getUserRecipe: Exception occurred", e)
            Result.failure(e)
        }
    }}