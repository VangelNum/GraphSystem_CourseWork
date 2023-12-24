package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset
import org.coursework.app.feature_core.data.DrawableItem

fun isPointInside(point: Offset, drawableItem: DrawableItem): Boolean {
    val offsetList = drawableItem.offsetList
    if (offsetList.size == 2) {
        val segmentStart = offsetList[0]
        val segmentEnd = offsetList[1]

        val minX = minOf(segmentStart.x, segmentEnd.x)
        val maxX = maxOf(segmentStart.x, segmentEnd.x)
        val minY = minOf(segmentStart.y, segmentEnd.y)
        val maxY = maxOf(segmentStart.y, segmentEnd.y)

        return point.x in minX..maxX && point.y in minY..maxY
    }

    var count = 0

    for (i in 0 until offsetList.size) {
        val current = offsetList[i]
        val next = offsetList[(i + 1) % offsetList.size]

        if ((current.y <= point.y && point.y < next.y || next.y <= point.y && point.y < current.y) &&
            point.x < (next.x - current.x) * (point.y - current.y) / (next.y - current.y) + current.x
        ) {
            count++
        }
    }

    return count % 2 != 0
}