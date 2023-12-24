package org.coursework.app.feature_cubic_spline.presentation

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import org.coursework.app.feature_core.data.DrawableItem

fun DrawScope.drawPathForCubicSpline(
    drawableItem: DrawableItem
) {
    drawPath(
        path = Path().apply {
            drawableItem.offsetList.forEachIndexed { index, offset ->
                if (index == 0) {
                    moveTo(offset.x, offset.y)
                } else {
                    lineTo(offset.x, offset.y)
                }
            }
        },
        color = drawableItem.color,
        style = Stroke(width = 2f)
    )
}