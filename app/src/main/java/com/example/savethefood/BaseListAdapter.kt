package com.example.savethefood.work

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Deprecated("The nested class in not correct, it always instantiate the base")
abstract class BaseListAdapter<T, DB: ViewDataBinding>(
    private val clickListener: BaseAdapterClickListener<T>
) : ListAdapter<T, BaseListAdapter.BaseViewHolder<T, DB>>(BaseDiffCallback<T>()) {

    protected abstract val layoutRes: Int
    private lateinit var dataBinding: DB

    override fun onBindViewHolder(holder: BaseViewHolder<T, DB>, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, DB> {
        return BaseViewHolder.from(parent, layoutRes)
    }

    open class BaseViewHolder<T, DB: ViewDataBinding> protected constructor(
        open val binding: DB
    ) : RecyclerView.ViewHolder(binding.root){

        open fun bind(clickListener: BaseAdapterClickListener<T>, item: T) {
            binding.executePendingBindings()
        }

        companion object {
            fun <T, DB: ViewDataBinding> from(parent: ViewGroup, layoutRes: Int): BaseViewHolder<T, DB> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<DB>(layoutInflater, layoutRes, parent, false)
                return BaseViewHolder(binding)
            }
        }
    }
}

class BaseDiffCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.toString() == newItem.toString()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}

class BaseAdapterClickListener<T>(
    val clickListener: (data: T) -> Unit
) {
    fun onClick(data: T) = clickListener(data)
}
