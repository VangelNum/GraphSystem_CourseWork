package org.coursework.app.feature_current_figure.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import org.coursework.app.feature_current_figure.data.CurrentFigure

fun DrawScope.drawPathAndPointsForCurrentFigure(currentFigure: CurrentFigure, color: Color) {
    drawPath(
        path = Path().apply {
            currentFigure.offsetList.forEachIndexed { index, offset ->
                if (index == 0) {
                    moveTo(offset.x, offset.y)
                } else {
                    lineTo(offset.x, offset.y)
                }
            }
        },
        color = color,
        style = Stroke(width = 4f)
    )
    currentFigure.offsetList.forEach { offset ->
        drawCircle(
            color = Color.Magenta,
            center = offset,
            radius = 4f
        )
    }
}