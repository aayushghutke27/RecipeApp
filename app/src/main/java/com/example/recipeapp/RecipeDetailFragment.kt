package com.example.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.recipeapp.databinding.FragmentRecipeDetaileBinding

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetaileBinding? = null
    private val binding get() = _binding!!

    private var recipe: Recipe? = null

    private var isDescriptionExpandable = false
    private var isIngredientExpandable = false
    private var isDirectionExpandable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipe = it.getParcelable("recipe", Recipe::class.java)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? MainActivity)?.hideBottomFragment()

        _binding = FragmentRecipeDetaileBinding.inflate(inflater, container, false)

        setUpUI()
        setUpClickListener()

        binding.content.setOnTouchListener { v, event ->
            binding.cardWatchVideo.visibility = View.GONE
            false
        }
        return binding.root

    }

    private fun setUpUI() {

        recipe?.let { recipe ->
            binding.recipeName.text = recipe.name
            binding.recipeTime.text = recipe.cookingDuration

            Glide.with(this)
                .load(recipe.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.recipeImg)

            binding.descriptionText.text = recipe.description
            binding.ingredientText.text = formatIngredient(recipe.ingredients)
            binding.directionText.text = recipe.directions
        }
    }


    private fun setUpClickListener() {

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnSeeMoreDescription.setOnClickListener {
            isDescriptionExpandable = !isDescriptionExpandable

            if (isDescriptionExpandable) {
                binding.descriptionText.maxLines = Int.MAX_VALUE
                binding.btnSeeMoreDescription.text = "See less"
            } else {
                binding.descriptionText.maxLines = 2
                binding.btnSeeMoreDescription.text = "See more"

            }
        }

        binding.ingredientHeading.setOnClickListener {
            toggleIngredient()
        }

        binding.icArrowIngredient.setOnClickListener {
            toggleIngredient()
        }

        binding.descriptionHeading.setOnClickListener {
            toggleDirection()
        }

        binding.icArrowDirection.setOnClickListener {
            toggleDirection()
        }
    }

    fun toggleIngredient() {

        isIngredientExpandable = !isIngredientExpandable

        if (isIngredientExpandable) {
            binding.ingredientText.visibility = View.VISIBLE
            binding.icArrowIngredient.rotation = 180f
        } else {

            binding.ingredientText.visibility = View.GONE
            binding.icArrowIngredient.rotation = 0f

        }

    }

    fun toggleDirection() {

        isDirectionExpandable = !isDirectionExpandable

        if (isDirectionExpandable) {
            binding.directionText.visibility = View.VISIBLE
            binding.icArrowDirection.rotation = 180f
        } else {
            binding.directionText.visibility = View.GONE
            binding.icArrowDirection.rotation = 0f
        }
    }

    fun formatIngredient(ingredients: String): String {
        return ingredients.split(",")
            .map { it.trim() }
            .joinToString("\n") { "• $it" }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        (activity as? MainActivity)?.showBottomFragment()
        _binding = null

    }

    companion object {

        fun newInstance(recipe: Recipe) =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("recipe", recipe)

                }
            }
    }

}
