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
import com.example.savethefood.BaseAdapter
import com.example.savethefood.constants.Constants.BUNDLE_KEY
import com.example.savethefood.constants.Constants.REQUEST_KEY
import com.example.savethefood.databinding.FragmentSearchableBinding
import com.example.savethefood.shared.viewmodel.AddFoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@AndroidEntryPoint
class SearchableFragment : DialogFragment() {

    private val viewModel: AddFoodViewModel by viewModels(ownerProducer = { requireParentFragment() })

    // Or we could use the lazy with Koin, but it is already created by Koin so no needed here:
    //override val viewModel: ChairViewModel by lazy {
    //    requireParentFragment().getViewModel()
    //}


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.75).toInt()
        dialog?.window?.setLayout(width, height)
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
                BaseAdapter.BaseClickListener {
                    setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to it))
                    dismiss()
                }
            )

            // todo move the listeners in bindadapters with high order in vm
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