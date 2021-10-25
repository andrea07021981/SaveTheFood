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
    //protected lateinit var dataBinding: DB // TODO make it private and pass it through the bind fun (holder not needed?) After taht, we can even remove the var databinding

    class BaseViewHolder<DB : ViewDataBinding>(
        val binding: DB,
    ) : RecyclerView.ViewHolder(binding.root) {

        // We need to keep the static call to avoid the error with binding and re-assignable value
        companion object {
            fun <DB: ViewDataBinding>from(parent: ViewGroup, layoutRes: Int): BaseViewHolder<DB> {
                val inflate = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<DB>(inflate, layoutRes, parent, false)
                return BaseViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DB> {
        return BaseViewHolder.from(parent, layoutRes)
    }

    abstract fun bind(
        holder: BaseViewHolder<DB>, // TODO can we remove it and pass only the binding?
        clickListener: BaseClickListener<T>,
        item: T
    )

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: BaseViewHolder<DB>, position: Int) {
        /*dataBinding = holder.binding
        bind(holder, onClickListener, getItem(position))
        dataBinding.executePendingBindings()*/
        with(holder.binding) {
            bind(holder, onClickListener, getItem(position))
            executePendingBindings()
        }
    }

    /**
     * Diff call back with high order to handle it based on the type of every single adapter
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

    // TODO remove the inherited clicklistener with two params, do like
    /**
     * open class BaseClickListener<T, R>(
    open val clickListener: (data: T, view: R?) -> Unit
    ) {
    fun onClick(data: T, view: R? = null) = clickListener(data, view)
    }
     */
}