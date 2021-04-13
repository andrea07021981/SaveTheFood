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
 */
abstract class BaseAdapter<T, DB: ViewDataBinding>(
    private val onClickListener: BaseClickListener<T>,
) : ListAdapter<T, BaseAdapter.BaseViewHolder<DB>>(BaseItemCallback<T>()) {

    protected abstract val layoutRes: Int
    protected lateinit var dataBinding: DB

    class BaseViewHolder<DB: ViewDataBinding>(
        val binding: DB,
    ) : RecyclerView.ViewHolder(binding.root) {

        // TODO find a way to use this like https://www.simplifiedcoding.net/abstract-recyclerview-adapter/
        /*companion object {
            fun <DB: ViewDataBinding> from(parent: ViewGroup, layoutRes: Int): BaseViewHolder<DB> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<DB>(layoutInflater, layoutRes, parent, false)
                return BaseViewHolder(binding)
            }
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DB> {
        //return BaseViewHolder.from(parent, layoutRes)
        val layoutInflater = LayoutInflater.from(parent.context)
        dataBinding = DataBindingUtil.inflate<DB>(layoutInflater, layoutRes, parent, false)
        return BaseViewHolder(dataBinding)
    }

    abstract fun bind(holder: BaseViewHolder<DB>, clickListener: BaseClickListener<T>, item: T)

    override fun onBindViewHolder(holder: BaseViewHolder<DB>, position: Int) {
        bind(holder, onClickListener, getItem(position))
    }

    class BaseItemCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.toString() == newItem.toString()

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    }

    class BaseClickListener<T>(
        val clickListener: (data: T) -> Unit
    ) {
        fun onClick(data: T) = clickListener(data)
    }
}