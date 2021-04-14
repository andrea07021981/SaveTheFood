package com.example.savethefood.cook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.EquipmentInstructionItemBinding
import com.example.savethefood.data.domain.EquipmentDomain
import com.example.savethefood.data.domain.IngredientsDomain
import com.example.savethefood.databinding.IngredientInstructionItemBinding

class EquipmentInstructionAdapter(
    onClickListener: BaseClickListener<EquipmentDomain>,
) : BaseAdapter<EquipmentDomain, EquipmentInstructionItemBinding>(onClickListener) {

    override val layoutRes: Int
        get() = R.layout.equipment_instruction_item

    override fun bind(
        holder: BaseViewHolder<EquipmentInstructionItemBinding>,
        clickListener: BaseClickListener<EquipmentDomain>,
        item: EquipmentDomain
    ) {
        with(dataBinding) {
            equipmentItem = item
        }
    }
}