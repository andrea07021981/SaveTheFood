package com.example.savethefood.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModel, DB : ViewDataBinding>() : Fragment() {

    protected abstract val viewModel: VM

    protected abstract val layoutRes: Int

    protected lateinit var binding: DB

    protected abstract val classTag: String

    fun init(inflater: LayoutInflater, container: ViewGroup) {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
    }

    open fun init() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        init(inflater, container!!)
        init()
        return binding.root
    }

    open fun refresh() {}
}