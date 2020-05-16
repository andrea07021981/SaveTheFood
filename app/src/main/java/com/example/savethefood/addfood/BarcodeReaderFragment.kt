package com.example.savethefood.addfood

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.savethefood.EventObserver
import com.example.savethefood.data.Result
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.databinding.FragmentBarcodereaderBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class BarcodeReaderFragment : Fragment() {

    private val barcodeReaderViewModel by viewModels<BarcodeReaderViewModel> {
        BarcodeReaderViewModel.BarcodeViewModelFactory(FoodDataRepository.getRepository(requireActivity().application))
    }

    private lateinit var dataBinding: FragmentBarcodereaderBinding

    //TODO add section for searching food by name and not code

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentBarcodereaderBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.barcodeReaderViewModel = barcodeReaderViewModel
        dataBinding.food = barcodeReaderViewModel.food.value
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        barcodeReaderViewModel.readBarcodeEvent.observe(this.viewLifecycleOwner, EventObserver {
            readBarcode()
        })
        barcodeReaderViewModel.goHomeEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController().popBackStack()
            }
        })

        barcodeReaderViewModel.progressVisibility.observe(this.viewLifecycleOwner, Observer {
            if (it) {
                dataBinding.loadingProgressbar.visibility = View.VISIBLE
            } else {
                dataBinding.loadingProgressbar.visibility = View.GONE
            }
        })

        barcodeReaderViewModel.barcodeResult.observe(this.viewLifecycleOwner, Observer {
            if (it is Result.Error || it is Result.ExError) {
                Toast.makeText(
                    context,
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun readBarcode() {
        FragmentIntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        //TODO forced value, emulator can't read barcode
        //041631000564
        result?.let {
            //TODO call a new method in vm, which gets the repository getApiFoodUpc and save. In the meantime use bindingadapger for a progress bar
            barcodeReaderViewModel.getApiFoodDetails(it.contents ?: "041631000564")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    inner class FragmentIntentIntegrator(private val fragment: Fragment) :
        IntentIntegrator(fragment.activity) {

        override fun startActivityForResult(intent: Intent, code: Int) {
            fragment.startActivityForResult(intent, code)
        }
    }
}