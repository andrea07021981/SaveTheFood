package com.example.savethefood.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.collection.ArraySet
import com.example.savethefood.R
import kotlinx.android.synthetic.main.adapter_food_spinner.view.*

/**
 * Custom adapter for food spinner
 * @param   ctx         current context
 * @param   foodItems   list of items
 */
class FoodSpinnerAdapter(
    ctx: Context,
    foodItems: ArraySet<FoodItem>
) : ArrayAdapter<FoodItem>(ctx, 0, foodItems.toList()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    private fun createItemView(position: Int, recycledView: View?, parent: ViewGroup):View {
        val foodItem = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.adapter_food_spinner,
            parent,
            false
        )

        foodItem?.let {
            view.icon.setImageResource(context.resources.getIdentifier(foodItem.img.id, "drawable", context.packageName))
            view.title.text = foodItem.name
        }
        return view
    }
}