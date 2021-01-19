package com.example.savethefood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController

// TODO add as base fragment everywhere. Create same way a generic baseVM
abstract class BaseFragment<VM : ViewModel, DB : ViewDataBinding>() : Fragment() {

    protected abstract val viewModel: VM

    protected abstract val layoutRes: Int

    private var _dataBinding: DB? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    protected val dataBinding: DB
        get() = _dataBinding!!

    protected abstract val classTag: String

    private fun baseInit(inflater: LayoutInflater, container: ViewGroup) {
        _dataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        _dataBinding?.let {
            it.lifecycleOwner = this
        }
    }

    open fun init() {

    }

    protected abstract fun activateObservers()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        baseInit(inflater, container!!)
        init()
        activateObservers()
        return dataBinding.root
    }

    open fun refresh() {}

    open fun <T> navigateTo(event: Event<T>?) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }
}