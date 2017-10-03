package io.jitrapon.android.template.util

import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView

/**
 * PagerSnapHelper that returns the currently snapped item's index
 *
 * @author Jitrapon Tiachunpun
 */
class CardSnapHelper : PagerSnapHelper() {

    private var snapPosition: Int = 0

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager?, velocityX: Int, velocityY: Int): Int {
        snapPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        return snapPosition
    }

    fun getSnapPosition(): Int = snapPosition
}
