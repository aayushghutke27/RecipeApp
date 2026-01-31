package com.example.recipeapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.databinding.FragmentMyRecipeBinding
import com.example.recipeapp.model.RecipeDataClass
import com.example.recipeapp.repository.FirebaseRepository
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyRecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyRecipeFragment : Fragment() {

    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!

    private val repository = FirebaseRepository()

    private lateinit var recipeAdapter: RecipeAdapter

    companion object {
        private const val TAG = "MyRecipeFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")
        setUpRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadRecipe()
    }

    private fun showRecipe(recipe: List<RecipeDataClass>) {
        Log.d(TAG, "Showing ${recipe.size} recipes")

        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.tvEmptySavedRecipe.visibility = View.GONE

        recipeAdapter.submitList(recipe)

    }

    private fun showEmptyState() {
        Log.d(TAG, "Showing empty state")

        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.tvEmptySavedRecipe.visibility = View.VISIBLE
        binding.tvEmptySavedRecipe.text = "No Recipes created yet click on + button to add new recipes"

    }

    private fun showError(errorMessage: String) {

        Log.e(TAG, "Error: $errorMessage")
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.tvEmptySavedRecipe.visibility = View.VISIBLE

        binding.tvEmptySavedRecipe.text = "Error: $errorMessage"
        Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_LONG).show()


    }

    private fun loadRecipe() {
        Log.d(TAG, "Loading recipes...")

        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        Log.d(TAG, "Current user: ${currentUser?.uid}")

        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.tvEmptySavedRecipe.visibility = View.GONE

        lifecycleScope.launch {
            try {
                Log.d(TAG, "Calling repository.getUserRecipe()")

                val result = repository.getUserRecipe()

                if (result.isSuccess) {
                    val recipe = result.getOrNull() ?: emptyList()

                    if (recipe.isNotEmpty()) {
                        showRecipe(recipe)
                    } else {
                        showEmptyState()
                    }
                } else {
                    val error = result.exceptionOrNull()
                    Log.e(TAG, "Error loading recipes: ${error?.message}", error)
                    showError(error?.message ?: "unknown error")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Exception loading recipes: ${e.message}", e)

                showError(e.message ?: "Failed to load recipe")

            }
        }
    }

    private fun setUpRecyclerView() {
        Log.d(TAG, "Setting up RecyclerView")
        recipeAdapter = RecipeAdapter (
            onRecipeClick = { recipe -> onRecipeClick(recipe) },
            onEditClick = { recipe -> onEditRecipe(recipe) },
            onDeleteClick = { recipe -> onDeleteRecipe(recipe) }

        )


        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = recipeAdapter
            setHasFixedSize(true)
        }



    }

    private fun onDeleteRecipe(recipe: RecipeDataClass) {

        AlertDialog.Builder(requireContext())
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete the recipe")
            .setPositiveButton("Delete"){_,_ ->
                performDelete(recipe)
            }
            .setNegativeButton("Cancel", null)
            .show()

    }

    private fun onEditRecipe(recipe: RecipeDataClass){
        Toast.makeText(
            requireContext(),"Edit Button is clicked",
            Toast.LENGTH_LONG
        ).show()

        val editRecipe = AddRecipeFragment.newInstance(recipe)
        editRecipe.show(childFragmentManager, "EditRecipeBottomSheet")

    }

    private fun onRecipeClick(recipeDataClass: RecipeDataClass) {
        Log.d(TAG, "Recipe clicked: ${recipeDataClass.recipeTitle}")

        Toast.makeText(
            requireContext(),
            "Recipe Clicked:${recipeDataClass.recipeTitle}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun performDelete(recipe: RecipeDataClass) {

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {

                val result = repository.deleteRecipe(recipe.id)
                binding.progressBar.visibility = View.GONE

                if (result.isSuccess){
                    Toast.makeText(requireContext(), "Recipe is deleted Successfully", Toast.LENGTH_LONG).show()
                    loadRecipe()
                }else{
                    val error = result.exceptionOrNull()
                    Toast.makeText(requireContext(), "Recipe is deleted failed", Toast.LENGTH_LONG).show()
                }

            }catch (e: Exception){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}




