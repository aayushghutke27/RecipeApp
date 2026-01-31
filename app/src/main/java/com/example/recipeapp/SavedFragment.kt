package com.example.recipeapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeapp.databinding.FragmentSavedBinding
import com.google.android.material.tabs.TabLayoutMediator


class SavedFragment : Fragment() {


    private var _binding: FragmentSavedBinding ?= null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter : ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAddRecipe()
        setUpViewPager()
    }

    private fun setUpViewPager() {

        viewPagerAdapter = ViewPagerAdapter(requireActivity())

        binding.viewpager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout ,binding.viewpager){ tab, position ->

            tab.text = when(position){
                0 -> "Saved Recipe"
                1 -> "My Recipe"
                else -> "Tab $position"
            }
            Log.d("SavedFragment", "Tab $position created: ${tab.text}")

        }.attach()
        Log.d("SavedFragment", "ViewPager setup complete")


    }

    private fun setUpAddRecipe() {
        binding.fbAddRecipe.setOnClickListener {

            val bottomSheet = AddRecipeFragment()
            bottomSheet.show(childFragmentManager, "AddRecipes")

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}