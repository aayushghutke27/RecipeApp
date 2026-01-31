package com.example.recipeapp

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.DialogFragment
import com.example.recipeapp.databinding.FragmentAddRecipeBinding
import com.example.recipeapp.model.RecipeDataClass
import com.example.recipeapp.repository.FirebaseRepository
import com.google.android.gms.common.internal.Objects
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AddRecipeFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddRecipeBinding? = null
    val binding get() = _binding!!

    private val repository = FirebaseRepository()

    private var existingRecipe: RecipeDataClass? = null
    private var isEditMode = false

    companion object{
        private var ARG_RECIPE = "recipe"

        fun newInstance(recipe: RecipeDataClass?=null): AddRecipeFragment{

            val fragment = AddRecipeFragment()

            if(recipe!=null){
                val bundle = Bundle().apply {
                    putParcelable(ARG_RECIPE, recipe)
                }
                fragment.arguments = bundle
            }

            return fragment
        }



    }


    override fun onStart() {
        super.onStart()

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        existingRecipe = arguments?.getParcelable(ARG_RECIPE, RecipeDataClass::class.java)
        isEditMode = existingRecipe != null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBottomSheet()
        setUpTextWatcher()
        setUpSaveButton()

        if(isEditMode){
            populateField()
        }

    }

    private fun populateField() {

        existingRecipe?.let { recipe ->

            binding.etRecipeTitle.setText(recipe.recipeTitle)
            binding.etDescription.setText(recipe.recipeDescription)
            binding.etCategory.setText(recipe.recipeCategory)
            binding.etPrepTime.setText(recipe.recipePrepTime)
            binding.etCookTime.setText(recipe.recipeCookTime)
            binding.etImageUrl.setText(recipe.recipeImgUrl)


            binding.tvTitle.text = "Edit Recipe"

            binding.btnSaveRecipe.text = "Update Recipe"
        }

    }



    private fun setUpSaveButton() {
        binding.btnSaveRecipe.setOnClickListener {
            if(isEditMode){
                editRecipe()
            }else{
                savedRecipe()
            }
        }
    }

    private fun editRecipe(){

        val title = binding.etRecipeTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val category = binding.etCategory.text.toString().trim()
        val prepTime = binding.etPrepTime.text.toString().trim()
        val cookTime = binding.etCookTime.text.toString().trim()
        val imgUrl = binding.etImageUrl.text.toString().trim()

        existingRecipe?.let { original ->

            val updatedRecipe = original.copy(

                recipeTitle = title,
                recipeDescription = description,
                recipeCategory = category,
                recipePrepTime = prepTime,
                recipeCookTime = cookTime,
                recipeImgUrl = imgUrl

            )

            binding.progressBar.visibility = View.VISIBLE
            binding.btnSaveRecipe.isEnabled = false

            CoroutineScope(Dispatchers.Main).launch {
                try {

                    val result = repository.updateRecipe(updatedRecipe)

                    binding.progressBar.visibility = View.GONE
                    binding.btnSaveRecipe.isEnabled = true

                    if(result.isSuccess){

                        Toast.makeText(requireContext(), "Recipe is successfully updated", Toast.LENGTH_LONG).show()
                        dismiss()

                    }else{
                        Toast.makeText(requireContext(), "Recipe is failed to updated", Toast.LENGTH_LONG).show()

                    }



                }catch (e: Exception){

                    binding.progressBar.visibility = View.GONE
                    binding.btnSaveRecipe.isEnabled = true
                    Toast.makeText(requireContext(), "Error ${e.message}", Toast.LENGTH_LONG).show()

                }
            }


        }

    }

    private fun savedRecipe() {
        val title = binding.etRecipeTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val category = binding.etCategory.text.toString().trim()
        val prepTime = binding.etPrepTime.text.toString().trim()
        val cookTime = binding.etCookTime.text.toString().trim()
        val imgUrl = binding.etImageUrl.text.toString().trim()


        val recipe = RecipeDataClass(

            recipeDescription = description,
            recipeCategory = category,
            recipePrepTime = prepTime,
            recipeCookTime = cookTime,
            recipeImgUrl = imgUrl,
            recipeTitle = title,

            )

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSaveRecipe.isEnabled = false

        CoroutineScope(Dispatchers.Main).launch {
            try {
                android.util.Log.d("AddRecipe", "Attempting to save recipe: $title")

                val result = repository.addRecipe(recipe)

                binding.progressBar.visibility = View.GONE
                binding.btnSaveRecipe.isEnabled = true

                if (result.isSuccess) {
                    android.util.Log.d("AddRecipe", "Recipe saved successfully!")
                    Toast.makeText(requireContext(), "Recipe Added!", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    val error = result.exceptionOrNull()
                    android.util.Log.e(
                        "AddRecipe",
                        "Failed to save recipe: ${error?.message}",
                        error
                    )
                    Toast.makeText(
                        requireContext(),
                        "Failed: ${error?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.btnSaveRecipe.isEnabled = true
                android.util.Log.e("AddRecipe", "Exception: ${e.message}", e)
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun setUpTextWatcher() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validInput()
            }
        }

        binding.etRecipeTitle.addTextChangedListener(watcher)
        binding.etDescription.addTextChangedListener(watcher)
        binding.etCategory.addTextChangedListener(watcher)
        binding.etPrepTime.addTextChangedListener(watcher)
        binding.etCookTime.addTextChangedListener(watcher)
        binding.etImageUrl.addTextChangedListener(watcher)

    }

    private fun validInput() {

        val title = binding.etRecipeTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val category = binding.etCategory.text.toString().trim()


        binding.btnSaveRecipe.isEnabled =
            title.isNotEmpty() && description.isNotEmpty() && category.isNotEmpty()

    }

    private fun setUpBottomSheet() {
        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog =
                dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog?.findViewById<View>(
                R.id.design_bottom_sheet
            )


        }
    }


}

