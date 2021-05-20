package com.example.savethefood

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.savethefood.util.*

/**
 * Abstract generic class for the dialogs.
 * @throws IllegalStateException if instantiated with object expression. Don't use as AnonymousClass
 */
abstract class BaseDialogFragment<DB: ViewDataBinding>(
    @LayoutRes open val layoutRes: Int,
    open val event: (Bundle) -> Unit
) : DialogFragment() {

    private var _dataBinding: DB? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    protected val dataBinding: DB
        get() = _dataBinding!!

    abstract val title: String
    protected var positiveButtonText: String = "Ok"
    protected var negativeButtonText: String = "Cancel"

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, null, false)
        _dataBinding?.let {
            it.lifecycleOwner = this
        }

        return buildDialog(requireContext()) {
            title(title)
            addCustomView(dataBinding.root)
            positiveButton(positiveButtonText) { dialog, _ ->
                event(createBundle())
                dialog.dismiss()
            }
            negativeButton(negativeButtonText) { dialog, _ -> dialog.dismiss() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }

    abstract fun createBundle(): Bundle
}