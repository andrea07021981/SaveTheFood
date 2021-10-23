package com.example.savethefood.cook

import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.IngredientInstructionItemBinding
import com.example.savethefood.shared.data.domain.IngredientsDomain

class IngredientInstructionAdapter(
    onClickListener: BaseClickListener<IngredientsDomain>,
) : BaseAdapter<IngredientsDomain, IngredientInstructionItemBinding>(
    onClickListener,
    { old, new -> old.ingredientId == new.ingredientId },
    { old, new -> old === new }) {

    override val layoutRes: Int
        get() = R.layout.ingredient_instruction_item

    override fun bind(
        holder: BaseViewHolder<IngredientInstructionItemBinding>,
        clickListener: BaseClickListener<IngredientsDomain>,
        item: IngredientsDomain
    ) {
        with(holder.binding) {
            ingredientItem = item
        }
    }
}