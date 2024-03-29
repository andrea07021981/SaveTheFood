package com.example.savethefood.home

import android.graphics.Rect
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpacesItemDecoration(private val mSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(@NonNull outRect: Rect, @NonNull view: View, @NonNull parent: RecyclerView, @NonNull state: RecyclerView.State) {
        outRect.left = mSpace
        outRect.right = mSpace
        outRect.bottom = mSpace

        // Add top margin only for the first items to avoid double space between items
        if (parent.layoutManager is StaggeredGridLayoutManager && parent.getChildAdapterPosition(
                view
            ) <= (parent.layoutManager as StaggeredGridLayoutManager).spanCount
        )
            outRect.top = mSpace
    }
}