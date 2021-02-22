package com.example.savethefood.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.savethefood.R

/**
 * UI custom container for UI forms
 */
class ContainerView : RelativeLayout {

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
        val parent = inflate(context, R.layout.container_view, this)

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ContainerView, defStyle, 0
        )

        if (a.hasValue(R.styleable.ContainerView_childView)) {
            val childView = a.getResourceId(
                R.styleable.ContainerView_childView,
                0
            )
            //TODO find a way to pass the binded values
            with(parent.findViewById<LinearLayout>(R.id.container_layout)) {
                addView(
                    LayoutInflater.from(context).inflate(childView, this, false)
                )
            }
        }
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }
}