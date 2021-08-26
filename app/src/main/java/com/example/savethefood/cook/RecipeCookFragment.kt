package com.example.savethefood.cook

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentRecipeCookBinding
import com.example.savethefood.shared.viewmodel.RecipeCookViewModel
import dagger.hilt.android.AndroidEntryPoint
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class RecipeCookFragment : BaseFragment<RecipeCookViewModel, FragmentRecipeCookBinding>() {

    private val args: RecipeCookFragmentArgs by navArgs()

    override val viewModel: RecipeCookViewModel by stateViewModel(
        state = { bundleOf("recipeInfoDomain" to args.recipeInfoDomain) },
        clazz = RecipeCookViewModel::class
    )

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