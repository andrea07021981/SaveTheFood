package com.example.savethefood.shopping

import com.example.savethefood.BaseAdapter
import com.example.savethefood.R
import com.example.savethefood.databinding.BagItemBinding
import com.example.savethefood.generated.callback.OnClickListener
import com.example.savethefood.shared.data.domain.BagDomain

class BagAdapter(
    val onClickListener: BaseClickListener<BagDomain>
) : BaseAdapter<BagDomain, BagItemBinding>(
    onClickListener,
    { old, new -> old.id == new.id },
    { old, new -> old === new }
){
    override val layoutRes: Int
        get() = R.layout.bag_item

    override fun bind(
        holder: BaseViewHolder<BagItemBinding>,
        clickListener: BaseClickListener<BagDomain>,
        item: BagDomain
    ) {
        with(holder.binding) {
            bagDomain = item
            bagCallback = clickListener
        }
    }
}