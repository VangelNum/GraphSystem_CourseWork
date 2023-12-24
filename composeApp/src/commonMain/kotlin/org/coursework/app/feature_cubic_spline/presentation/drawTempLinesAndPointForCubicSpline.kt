package org.coursework.app.feature_cubic_spline.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.coursework.app.feature_current_figure.data.CurrentFigure

fun DrawScope.drawTempLinesAndPointForCubicSpline(
    currentFigure: CurrentFigure
) {
    currentFigure.tempListForLinesCubicSpline.forEachIndexed { index, offset ->
        if (index < currentFigure.tempListForLinesCubicSpline.size - 1 && index % 2 == 0) {
            drawLine(
                color = Color.Cyan,
                start = currentFigure.tempListForLinesCubicSpline[index],
                end = currentFigure.tempListForLinesCubicSpline[index + 1],
                strokeWidth = 2f
            )
        }
        drawCircle(
            color = Color.Magenta,
            center = offset,
            radius = 4f
        )
    }
}