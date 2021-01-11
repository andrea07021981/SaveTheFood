package com.example.savethefood.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.savethefood.databinding.AdapterFoodSpinnerBinding

// ADD two way data binding observers
/**
 * Custom adapter for food spinner. We can use the viewbinding instead databinding since we don't
 * need to bind data in layout
 * @param   ctx         current context
 * @param   foodItems   list of items
 */
class FoodSpinnerAdapter(
    ctx: Context,
    foodItems: List<FoodItem>
) : ArrayAdapter<FoodItem>(ctx, 0, foodItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup):View {
        val foodItem = getItem(position)
        val viewDataBinding  = convertView?.let {
            AdapterFoodSpinnerBinding.bind(it)
        } ?: AdapterFoodSpinnerBinding.inflate(LayoutInflater.from(context), parent, false)
        foodItem?.let {
            viewDataBinding.icon.setImageResource(context.resources.getIdentifier(foodItem.img.id, "drawable", context.packageName))
            viewDataBinding.title.text = foodItem.name
        }
        return viewDataBinding.root
    }
}