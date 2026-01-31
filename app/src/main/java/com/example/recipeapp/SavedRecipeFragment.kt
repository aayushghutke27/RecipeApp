package com.example.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.databinding.FragmentSavedBinding
import com.example.recipeapp.databinding.FragmentSavedRecipeBinding
import com.example.recipeapp.model.RecipeDataClass
import kotlinx.coroutines.launch


class SavedRecipeFragment : Fragment() {

    private var _binding : FragmentSavedRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var savedRecipeRepository: SavedRecipeRepository

    private lateinit var savedRecipeAdapter: SavedRecipeAdapter

    private val savedRecipeId = mutableSetOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedRecipeRepository = SavedRecipeRepository(requireContext())

        setUpRecyclerView()
        observeSavedRecipe()

    }

    private fun setUpRecyclerView(){
        savedRecipeAdapter = SavedRecipeAdapter(
            onRecipeClick = {recipe ->
                onRecipeClick(recipe)
            },
            onFavoriteClick = { recipe ->
                onFavoriteClick(recipe)
            },
            isFavorite = { recipeId ->
                savedRecipeId.contains(recipeId)
            }
        )

        binding.savedRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = savedRecipeAdapter
            setHasFixedSize(true)

        }
    }

    private fun observeSavedRecipe(){
        savedRecipeRepository.getAllSavedRecipe().observe(viewLifecycleOwner){savedEntities ->

            val recipes =  savedEntities.map { entity ->
                RecipeDataClass(
                    id =  entity.id,
                    recipeTitle = entity.recipeTitle,
                    recipeDescription = entity.recipeDescription,
                    recipeCategory = entity.recipeCategory,
                    recipePrepTime = entity.recipePrepTime,
                    recipeCookTime = entity.recipeCookTime,
                    recipeImgUrl = entity.recipeImageUrl,
                    userId = "",
                    createdAt = entity.savedAt,
                    updatedAt = entity.savedAt
                )

            }

            savedRecipeId.clear()
            savedRecipeId.addAll(recipes.map { it.id })

            savedRecipeAdapter.submitList(recipes)

            if(recipes.isEmpty()){
                binding.tvEmptySavedRecipe.visibility = View.VISIBLE
                binding.savedRecyclerView.visibility = View.GONE
                binding.tvEmptySavedRecipe.text = "There is no saved recipe \n you can start saving the your fav recipes"
            }else{
                binding.tvEmptySavedRecipe.visibility = View.GONE
                binding.savedRecyclerView.visibility = View.VISIBLE
            }


        }


    }


    private fun onRecipeClick(recipe: RecipeDataClass) {
        Toast.makeText(requireContext(), "Recipe:${recipe.recipeTitle}", Toast.LENGTH_SHORT).show()
    }

    private fun onFavoriteClick(recipe: RecipeDataClass){
        lifecycleScope.launch {
            try {
                savedRecipeRepository.unsavedRecipe(recipe.id)
                Toast.makeText(requireContext(), "Recipe ${recipe.recipeTitle} is Removed From Saved Recipe",
                    Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                Toast.makeText(requireContext(), "Error ${e.message}",
                    Toast.LENGTH_SHORT ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }







}



