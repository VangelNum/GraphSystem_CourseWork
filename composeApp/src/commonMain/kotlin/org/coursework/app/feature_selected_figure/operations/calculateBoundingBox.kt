package org.coursework.app.feature_selected_figure.operations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.max
import kotlin.math.min

fun calculateBoundingBox(offsetList: List<Offset>): Rect {
    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE

    for (offset in offsetList) {
        minX = min(minX, offset.x)
        minY = min(minY, offset.y)
        maxX = max(maxX, offset.x)
        maxY = max(maxY, offset.y)
    }

    return Rect(minX, minY, maxX, maxY)
}
