package com.example.savethefood.recipe

import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.constants.RecipeType
import com.example.savethefood.constants.StorageType
import com.example.savethefood.databinding.CustomTabLayoutBinding
import com.example.savethefood.databinding.FragmentReceipeBinding
import com.example.savethefood.databinding.FragmentRecipeContainerBinding
import com.example.savethefood.home.HomeFragmentContainerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragmentContainer : BaseFragment<RecipeViewModel, FragmentRecipeContainerBinding>(){

    override val viewModel by viewModels<RecipeViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_recipe_container

    override val classTag: String
        get() = RecipeFragmentContainer::class.java.simpleName

    override fun init() {
        with(dataBinding) {
            recipeViewPager.adapter = RecipeFragmentContainerAdapter(childFragmentManager, lifecycle)
            TabLayoutMediator(recipeTabLayout, recipeViewPager) { tab, position ->
                val tabCustomView = CustomTabLayoutBinding.inflate(layoutInflater)
                tabCustomView.titleTextView.text = RecipeType.values()[position].type
                tab.customView = tabCustomView.root
                tab.tag = RecipeType.values()[position].type
            }.attach()
        }
        setHasOptionsMenu(true)
    }

    override fun activateObservers() {
        viewModel.listByRecipeType.observe(viewLifecycleOwner) {
            if (it != null) {
                for ((index, value) in it) {
                    val linearLayout = dataBinding.recipeTabLayout.getTabAt(index.ordinal)
                        ?.customView as LinearLayout
                    CustomTabLayoutBinding.bind(linearLayout).countTextView.text =
                        value.toString()
                }
            }
        }
    }
}