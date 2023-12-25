package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset
import org.coursework.app.feature_core.data.DrawableItem
import kotlin.math.max
import kotlin.math.min

fun getCenterOfFigure(figure: DrawableItem): Offset {
    if (figure.offsetList.isEmpty()) {
        return Offset(0f, 0f)
    }

    var minX = Float.MAX_VALUE
    var minY = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var maxY = Float.MIN_VALUE

    for (offset in figure.offsetList) {
        minX = min(minX, offset.x)
        minY = min(minY, offset.y)
        maxX = max(maxX, offset.x)
        maxY = max(maxY, offset.y)
    }

    val centerX = (minX + maxX) / 2
    val centerY = (minY + maxY) / 2

    return Offset(centerX, centerY)
}