package com.example.savethefood.cook

import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.EquipmentInstructionItemBinding
import com.example.savethefood.shared.data.domain.EquipmentDomain

class EquipmentInstructionAdapter(
    onClickListener: BaseClickListener<EquipmentDomain>,
) : BaseAdapter<EquipmentDomain, EquipmentInstructionItemBinding>(
    onClickListener,
    { old, new -> old.equipmentId == new.equipmentId },
    { old, new -> old === new }) {

    override val layoutRes: Int
        get() = R.layout.equipment_instruction_item

    override fun bind(
        holder: BaseViewHolder<EquipmentInstructionItemBinding>,
        clickListener: BaseClickListener<EquipmentDomain>,
        item: EquipmentDomain
    ) {
        with(holder.binding) {
            equipmentItem = item
        }
    }
}