package com.example.savethefood.plan

import androidx.fragment.app.viewModels
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentPlanBinding
import com.example.savethefood.shared.viewmodel.PlanViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanFragment : BaseFragment<PlanViewModel, FragmentPlanBinding>() {

    override val viewModel: PlanViewModel by viewModel()

    override val layoutRes: Int
        get() = R.layout.fragment_plan

    override val classTag: String
        get() = PlanFragment::class.java.simpleName

    override fun init() {
        with(dataBinding) {
            plaViewModel = viewModel
        }
    }

    override fun activateObservers() {

    }
}