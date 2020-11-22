package com.example.savethefood.food

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.EventObserver
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.databinding.FragmentFoodBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Deprecated("For now it's not used to search food with spoon api")
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FoodFragment : Fragment() {

    private val foodViewModel: FoodViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentFoodBinding.inflate(inflater).also {
            it.lifecycleOwner = this
            it.foodViewModel = foodViewModel
            it.recyclerView.layoutManager = LinearLayoutManager(activity)
            it.recyclerView.adapter = FoodSearchAdapter(FoodSearchAdapter.OnClickListener { productDomain ->
                productDomain.let {
                    AlertDialog.Builder(requireNotNull(activity))
                        .setTitle("Save food")
                        .setMessage("Save ${productDomain.title}?")
                        .setCancelable(false)
                        .setNegativeButton("Cancel") { dialogInterface, _ ->
                            dialogInterface.cancel()
                        }
                        .setPositiveButton("Confirm") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                            foodViewModel.saveFoodDetail(it)
                        }
                        .create()
                        .show()
                }
            })
        }

        activateObservers()
        return dataBinding.root
    }

    private fun activateObservers() {
        foodViewModel.search.observe(this.viewLifecycleOwner, EventObserver{
            it.let {
                val inputManager: InputMethodManager =
                    requireNotNull(activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    requireNotNull(activity).currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        })
        foodViewModel.foodDomain.observe(this.viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }
}

