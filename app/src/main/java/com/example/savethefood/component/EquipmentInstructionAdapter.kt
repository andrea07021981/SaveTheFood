package com.example.savethefood.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.savethefood.databinding.EquipmentInstructionItemBinding
import com.example.savethefood.local.domain.EquipmentDomain

class EquipmentInstructionAdapter : ListAdapter<EquipmentDomain, EquipmentInstructionAdapter.EquipmentInstructionViewHolder>(DiffCallback) {

    class EquipmentInstructionViewHolder private constructor(
        val binding: EquipmentInstructionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EquipmentDomain) {
            with(binding) {
                equipmentItem = item
                executePendingBindings()
            }
        }

        companion object {
            val from = {parent: ViewGroup ->
                val layoutInflate = LayoutInflater.from(parent.context)
                val binding = EquipmentInstructionItemBinding.inflate(layoutInflate, parent, false)
                EquipmentInstructionViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentInstructionViewHolder {
        return EquipmentInstructionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EquipmentInstructionViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Food]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<EquipmentDomain>() {
        override fun areItemsTheSame(oldItem: EquipmentDomain, newItem: EquipmentDomain): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: EquipmentDomain, newItem: EquipmentDomain): Boolean {
            return oldItem.equipmentId == newItem.equipmentId
        }
    }

}