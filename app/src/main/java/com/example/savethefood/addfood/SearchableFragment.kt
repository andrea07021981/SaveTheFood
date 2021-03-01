package com.example.savethefood.addfood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.databinding.FragmentSearchableBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchableFragment : DialogFragment() {

    private val viewModel: AddFoodViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentSearchableBinding.inflate(inflater, container, false)
        with(view) {
            lifecycleOwner = viewLifecycleOwner
            addFoodViewModel = viewModel
            foodsRecycleview.layoutManager = LinearLayoutManager(activity)
            foodsRecycleview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            foodsRecycleview.adapter = FoodTypeAdapter(
                FoodTypeAdapter.OnClickListener {
                    setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to it))
                    dismiss()
                }
            )

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.updateFilter(query ?: "")
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.updateFilter(newText ?: "")
                    return false
                }

            })
            searchView.setQuery("", true)
        }
        return view.root
    }
}