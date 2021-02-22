package com.example.savethefood.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.view.children
import androidx.core.view.get
import com.example.savethefood.R
import com.example.savethefood.addfood.AddFoodViewModel
import com.example.savethefood.databinding.ContainerViewBinding
import com.example.savethefood.databinding.DatePickerLayoutBinding
import kotlinx.android.synthetic.main.container_view.view.*

/**
 * UI custom container for UI forms
 *
 * Example of UI
 * <com.example.savethefood.ui.ContainerView
android:layout_width="match_parent"
android:layout_height="wrap_content"
app:addFoodViewModel="@{addFoodViewModel}"
app:childView="@layout/date_picker_layout"/>
 */
class ContainerView : RelativeLayout {

    private lateinit var binding: ContainerViewBinding
    private lateinit var datePickerBinding: DatePickerLayoutBinding

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        binding = ContainerViewBinding.inflate(LayoutInflater.from(context), this, false)

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ContainerView, defStyle, 0
        )

        if (a.hasValue(R.styleable.ContainerView_childView)) {
            val childView = a.getResourceId(
                R.styleable.ContainerView_childView,
                0
            )
            //TODO generalize the add child and vm. Create custom UIs for children?
            datePickerBinding = DatePickerLayoutBinding.inflate(
                LayoutInflater.from(context),
                this,
                false
            )
            with(binding.root) {
                addView(
                    datePickerBinding.root,
                    0
                )
            }
        }
        a.recycle()
    }

    fun setAddFoodViewModel(viewModel: AddFoodViewModel) {
        datePickerBinding.addFoodViewModel = viewModel
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }
}