package com.example.savethefood.cook

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.data.domain.StepDomain
import com.example.savethefood.databinding.FragmentRecipeCookBinding
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener

@AndroidEntryPoint
class RecipeCookFragment : BaseFragment<RecipeCookViewModel, FragmentRecipeCookBinding>() {

    private val args: RecipeCookFragmentArgs by navArgs()

    override val viewModel by viewModels<RecipeCookViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_recipe_cook

    override val classTag: String
        get() = RecipeCookFragment::class.java.simpleName

    override fun init() {
        with(dataBinding) {
            recipeCookViewModel = viewModel
        }
    }

    override fun activateObservers() {
        viewModel.recipeInfoDomain.observe(viewLifecycleOwner) {
            it?.let {
                val stepList = arrayListOf<CookStepper>()
                it.recipeAnalyzedInstructions.forEach { analyzedInstructions ->
                    for (step in analyzedInstructions.instructionSteps) {
                        stepList.add(
                            CookStepper(
                                step.stepStep,
                                step
                            )
                        )
                    }
                }

                // Find the form view, set it up and initialize it.
                val verticalStepperForm = dataBinding.stepperForm
                verticalStepperForm
                    .setup(object : StepperFormListener {
                        override fun onCompletedForm() {
                            findNavController().popBackStack()
                        }

                        override fun onCancelledForm() {

                        }

                    }, *stepList.toTypedArray())
                    .init();
            }
        }
    }
}