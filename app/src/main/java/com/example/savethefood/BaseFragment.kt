package com.example.savethefood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

// TODO add as base fragment everywhere. Create same way a generic baseVM
abstract class BaseFragment<VM : ViewModel, DB : ViewDataBinding>() : Fragment() {

    protected abstract val viewModel: VM

    protected abstract val layoutRes: Int

    private var _dataBinding: DB? = null
    protected val dataBinding get() = _dataBinding!!

    protected abstract val classTag: String

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        _dataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        dataBinding.lifecycleOwner = this
    }

    open fun init() {}

    protected abstract fun activateObservers()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        init(inflater, container!!)
        init()
        activateObservers()
        return dataBinding.root
    }

    open fun refresh() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }
}