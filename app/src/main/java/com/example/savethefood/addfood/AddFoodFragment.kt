package com.example.savethefood.addfood

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.savethefood.BaseFragment
import com.example.savethefood.EventObserver
import com.example.savethefood.Notifier
import com.example.savethefood.R
import com.example.savethefood.constants.Constants.BUNDLE_KEY
import com.example.savethefood.constants.Constants.REQUEST_KEY
import com.example.savethefood.data.Result
import com.example.savethefood.data.domain.FoodItem
import com.example.savethefood.databinding.FragmentAddFoodBinding
import com.google.android.material.transition.MaterialFadeThrough
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AddFoodFragment : BaseFragment<AddFoodViewModel, FragmentAddFoodBinding>() {

    private val args: AddFoodFragmentArgs by navArgs()

    private enum class ShortCutState {
        SHORT_LINK,
        DIRECT
    }
    private var shortCutState = ShortCutState.DIRECT

    private lateinit var startBarcodeForResult: ActivityResultLauncher<Intent>

    override val viewModel by viewModels<AddFoodViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_add_food

    override val classTag: String
        get() = AddFoodFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            addTarget(R.id.food_item_root)
            duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }
    }

    override fun init() {
        super.init()

        with(dataBinding) {
            addFoodViewModel = viewModel
        }
        setHasOptionsMenu(true)

        // If the intent data is not null we are in shortcut mode
        if (requireActivity().intent.data != null) {
            shortCutState = ShortCutState.SHORT_LINK
        }

        startBarcodeForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            val result: IntentResult? =
                IntentIntegrator.parseActivityResult(it.resultCode, it.resultCode, it.data)

            //TODO forced value, emulator can't read barcode
            //041631000564
            result?.let { intentResult ->
                viewModel.getApiFoodDetails(intentResult.contents ?: "041631000564")
            }
        };
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.get(BUNDLE_KEY) as FoodItem
            viewModel.updateFood(result)
        }
    }

    override fun activateObservers() {
        viewModel.openFoodTypeDialog.observe(viewLifecycleOwner, EventObserver {
            SearchableFragment().show(parentFragmentManager, classTag)
        })

        viewModel.openDateDialog.observe(viewLifecycleOwner, EventObserver {
            with(Calendar.getInstance()) {
                time = it
                DatePickerDialog(
                    requireContext(),
                    R.style.DatePickerTheme,
                    { _, year, month, dayOfMonth ->
                        // With SDK > O
                        //LocalDate.of(year, month, dayOfMonth)

                            this.set(year, month, dayOfMonth)
                            viewModel.updateBestBefore(this.time)
                    }, get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        viewModel.foodDomain.observe(viewLifecycleOwner) {
            Log.d(classTag, "Updated item: $it")
        }

        viewModel.barcodeFoodEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                startBarcodeForResult.launch(IntentIntegrator(activity).createScanIntent())
            }
        })

        viewModel.newFoodFoodEvent.observe(viewLifecycleOwner) {
            // TODO ask to fill the values with the result
        }

        viewModel.saveFoodEvent.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    if (shortCutState == ShortCutState.SHORT_LINK) {
                        val bundle = bundleOf("foodDomain" to it.data)
                        val pendingIntent = findNavController()
                            .createDeepLink()
                            .setDestination(R.id.addFoodFragment)
                            .setArguments(bundle)
                            .createPendingIntent()
                        Notifier.postNotification(it.data.id, requireContext(), pendingIntent)
                    }
                    findNavController().popBackStack()
                }
                is Result.ExError, is Result.Error -> Toast.makeText(
                    context,
                    "Error saving food",
                    Toast.LENGTH_LONG
                ).show()
                else -> Unit
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_barcode, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.barcode) {
            viewModel.navigateToBarcodeReader()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
