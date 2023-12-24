package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset
import org.coursework.app.feature_core.data.DrawableItem

fun getCenterOfFigure(figure: DrawableItem): Offset {
    var sumX = 0f
    var sumY = 0f

    for (offset in figure.offsetList) {
        sumX += offset.x
        sumY += offset.y
    }

    val centerX = sumX / figure.offsetList.size
    val centerY = sumY / figure.offsetList.size

    return Offset(centerX, centerY)
}