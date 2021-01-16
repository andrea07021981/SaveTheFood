package com.example.savethefood.addfood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.ArraySet
import androidx.collection.arraySetOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentSearchableBinding
import com.example.savethefood.ui.FoodItem
import com.example.savethefood.util.FoodImage
import kotlinx.android.synthetic.main.fragment_food.*
import okhttp3.internal.notify

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
            addFoodViewModel = viewModel
            foodsRecycleview.layoutManager = LinearLayoutManager(activity)
            foodsRecycleview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            foodsRecycleview.adapter = FoodTypeAdapter(
                FoodTypeAdapter.OnClickListener {

                }
            )
        }
        return view.root
    }
}