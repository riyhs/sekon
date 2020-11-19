package com.sekon.app.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecorationVertical(private val spaceHeight: Int,private val isReversed: Boolean) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (isReversed) {
                if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) {
                    top = spaceHeight
                }
                bottom = spaceHeight
            } else {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceHeight
                }
                bottom = spaceHeight
            }

            left = spaceHeight
            right = spaceHeight
        }
    }
}