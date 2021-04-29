package com.example.savethefood

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Base adapter with T type and DB for binding
 * Try to add this as generic https://medium.com/coding-blocks/implementing-selection-in-recyclerview-36a9739844e0
 * must change inner BaseViewHolder amd : ListAdapter<T, BaseAdapter.BaseViewHolder<DB>>( to
 * ListAdapter<T, BaseAdapter<T, DB>.BaseViewHolder<DB>>(
 * Add the tracker to the subclasses is easier?
 */
abstract class BaseAdapter<T, DB : ViewDataBinding>(
    private val onClickListener: BaseClickListener<T>,
    compareItems: (old: T, new: T) -> Boolean,
    compareContents: (old: T, new: T) -> Boolean
) : ListAdapter<T, BaseAdapter.BaseViewHolder<DB>>(
    BaseItemCallback<T>(
        compareItems,
        compareContents
    )
) {

    protected abstract val layoutRes: Int
    protected lateinit var dataBinding: DB

    class BaseViewHolder<DB : ViewDataBinding>(
        val binding: DB,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DB> {
        val layoutInflater = LayoutInflater.from(parent.context)
        dataBinding = DataBindingUtil.inflate<DB>(layoutInflater, layoutRes, parent, false)
        return BaseViewHolder(dataBinding)
    }

    abstract fun bind(holder: BaseViewHolder<DB>, clickListener: BaseClickListener<T>, item: T)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: BaseViewHolder<DB>, position: Int) {
        bind(holder, onClickListener, getItem(position))
        dataBinding.executePendingBindings()
    }

    /**
     * Diffcall back with high order to handle it based on the type of every single adapter
     */
    class BaseItemCallback<T>(
        private val compareItems: (old: T, new: T) -> Boolean,
        private val compareContents: (old: T, new: T) -> Boolean
    ) : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) = compareItems(oldItem, newItem)

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T) = compareContents(oldItem, newItem)
    }

    open class BaseClickListener<T>(
        open val clickListener: (data: T) -> Unit
    ) {
        fun onClick(data: T) = clickListener(data)
    }
}