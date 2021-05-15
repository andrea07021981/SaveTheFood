package com.example.savethefood.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.savethefood.constants.Constants
import com.example.savethefood.constants.RecipeType

class RecipeFragmentContainerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = RecipeType.values().size

    override fun createFragment(position: Int): Fragment {
        val recipeType: RecipeType = when (position) {
            0 -> RecipeType.REMOTE
            else -> RecipeType.LOCAL
        }
        return RecipeFragment().apply {
            arguments = Bundle().apply {
                putSerializable(Constants.RECIPE_LIST, recipeType)
            }
        }
    }
}