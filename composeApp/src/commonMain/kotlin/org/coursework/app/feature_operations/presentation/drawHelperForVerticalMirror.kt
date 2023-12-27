package org.coursework.app.feature_operations.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.coursework.app.feature_core.data.DrawableItem

fun DrawScope.drawHelperForVerticalMirror(
    position: Offset,
    selectedFigure: DrawableItem?,
    showCheckBoxForVerticalLine: Boolean,
    showPoint: Boolean,
) {
    if (showCheckBoxForVerticalLine) {
        selectedFigure?.let { figure ->
            val axisX = position.x
            drawLine(
                color = Color.Cyan,
                start = Offset(axisX, figure.offsetList.minOf { it.y }),
                end = Offset(axisX, figure.offsetList.maxOf { it.y }),
                strokeWidth = 2f
            )
        }
    } else {
        if (showPoint) {
            drawCircle(
                color = Color.Red,
                center = position,
                radius = 4f
            )
        }
    }
}