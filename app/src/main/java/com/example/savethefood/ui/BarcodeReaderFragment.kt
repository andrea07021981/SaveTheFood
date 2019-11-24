package com.example.savethefood.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.savethefood.databinding.FragmentBarcodereaderBinding
import com.example.savethefood.local.domain.FoodDomain
import com.example.savethefood.viewmodel.BarcodeReaderViewModel
import com.example.savethefood.viewmodel.HomeViewModel
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.fragment_barcodereader.*


class BarcodeReaderFragment : Fragment() {

    private val barcodeReaderViewModel: BarcodeReaderViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this, BarcodeReaderViewModel.Factory(activity.application)).get(BarcodeReaderViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentBarcodereaderBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.barcodeReaderViewModel = barcodeReaderViewModel
        dataBinding.food = barcodeReaderViewModel.food

        barcodeReaderViewModel.startReadingBarcode.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                readBarcode()
                barcodeReaderViewModel.doneReadBarcode()
            }
        })
        barcodeReaderViewModel.popToHome.observe(this.viewLifecycleOwner, Observer {
            it?.let {
                findNavController().popBackStack()
                barcodeReaderViewModel.donePopToHome()
            }
        })

        //TODO solve the problem of two button with the keyboard opened
        return dataBinding.root
    }

    private fun readBarcode() {
        FragmentIntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

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