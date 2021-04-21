package com.example.savethefood.plan

import androidx.fragment.app.viewModels
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentPlanBinding

class PlanFragment : BaseFragment<PlanViewModel, FragmentPlanBinding>() {

    override val viewModel: PlanViewModel by viewModels()

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