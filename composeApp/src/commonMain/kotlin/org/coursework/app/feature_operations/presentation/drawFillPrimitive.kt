package org.coursework.app.feature_operations.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.coursework.app.feature_core.data.DrawableItem
import org.coursework.app.feature_operations.operations.getMaximumY
import org.coursework.app.feature_operations.operations.getMinimumY
import org.coursework.app.feature_tmo.data.Line
import org.coursework.app.feature_tmo.operations.calculateLines
import org.coursework.app.feature_tmo.operations.calculateOffsets

fun DrawScope.fillPrimitive(drawableItem: DrawableItem) {
    if (drawableItem.offsetList.size == 2) {
        drawLine(
            color = drawableItem.color,
            strokeWidth = 2f,
            start = drawableItem.offsetList[0],
            end = drawableItem.offsetList[1]
        )
        return
    }
    if (drawableItem.tmoWasMake == true) {
        val offsets = drawableItem.offsetList
        offsets.chunked(2) { (xl, xr) ->
            drawLine(
                color = drawableItem.color,
                strokeWidth = 2f,
                start = xl,
                end = xr
            )
        }
    } else {
        val offsetList = drawableItem.offsetList
        val minY = getMinimumY(offsetList)
        val maxY = getMaximumY(offsetList)
        val xb = mutableListOf<Float>()

        for (y in minY..maxY) {
            xb.clear()

            for (i in offsetList.indices) {
                //для каждой вершины определяется индекс следующей вершины лежащей на этом отрезке
                val k = if (i < offsetList.size - 1) i + 1 else 0
                //пересекает ли текущая линия многоугольника сканирующую строку y
                if ((offsetList[i].y > y && offsetList[k].y <= y) || (offsetList[k].y > y && offsetList[i].y <= y)) {
                    //Вычисляется x-координата точки пересечения линии с текущей строкой,
                    //используя линейную интерполяцию между двумя соседними вершинами многоугольника.
                    val x = offsetList[i].x + (y - offsetList[i].y) / (offsetList[k].y - offsetList[i].y) * (offsetList[k].x - offsetList[i].x)
                    xb.add(x)
                }
            }
            xb.sort()

            for (i in xb.indices step 2) {
                val xl = xb[i].toInt()
                val xr = if (i + 1 < xb.size) xb[i + 1].toInt() else xl
                drawLine(
                    color = drawableItem.color,
                    strokeWidth = 2f,
                    start = Offset(xl.toFloat(), y.toFloat()),
                    end = Offset(xr.toFloat(), y.toFloat())
                )
            }
        }
    }
}
