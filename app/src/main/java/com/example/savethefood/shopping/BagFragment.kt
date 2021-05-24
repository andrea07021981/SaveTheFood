package com.example.savethefood.shopping

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.savethefood.BaseFragment
import com.example.savethefood.FragmentCallback
import com.example.savethefood.R
import com.example.savethefood.data.Result
import com.example.savethefood.databinding.FragmentBagBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BagFragment : BaseFragment<BagViewModel, FragmentBagBinding>(), FragmentCallback {

    override val viewModel: BagViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_bag

    override val classTag: String
        get() = BagFragment::class.java.simpleName

    override fun init() {
        with(dataBinding) {

        }
    }

    override fun activateObservers() {

    }

    override fun onAddClicked(view: View) {
        TODO("Not yet implemented, call the add food in bag")
    }
}