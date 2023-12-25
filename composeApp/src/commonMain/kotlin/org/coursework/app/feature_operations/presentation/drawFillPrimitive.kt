package org.coursework.app.feature_operations.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.coursework.app.feature_core.data.DrawableItem
import org.coursework.app.feature_operations.operations.getMaximumY
import org.coursework.app.feature_operations.operations.getMinimumY
import org.coursework.app.feature_tmo.data.Line
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
        drawableItem.lines.clear()
        for (i in offsets.indices step 2) {
            val xl = offsets[i].x
            val xr = if (i + 1 < offsets.size) offsets[i + 1].x else xl
            drawLine(
                color = drawableItem.color,
                strokeWidth = 2f,
                start = Offset(xl, offsets[i].y),
                end = Offset(xr, offsets[i + 1].y)
            )
            drawableItem.lines.add(Line(xl = xl.toInt(), xr = xr.toInt(), y = offsets[i].y.toInt()))
        }
    } else {
        drawableItem.lines.clear()

        val offsetList = drawableItem.offsetList
        val minY = getMinimumY(offsetList)
        val maxY = getMaximumY(offsetList)
        val xb = mutableListOf<Float>()

        for (y in minY..maxY) {
            xb.clear()

            for (i in offsetList.indices) {
                val k = if (i < offsetList.size - 1) i + 1 else 0

                if ((offsetList[i].y > y && offsetList[k].y <= y) || (offsetList[k].y > y && offsetList[i].y <= y)) {
                    val x =
                        offsetList[i].x + (y - offsetList[i].y) / (offsetList[k].y - offsetList[i].y) * (offsetList[k].x - offsetList[i].x)
                    xb.add(x)
                }
            }

            xb.sort()

            // Add Offset values in the correct order
            for (i in xb.indices step 2) {
                val xl = xb[i].toInt()
                val xr = if (i + 1 < xb.size) xb[i + 1].toInt() else xl
                drawLine(
                    color = drawableItem.color,
                    strokeWidth = 2f,
                    start = Offset(xl.toFloat(), y.toFloat()),
                    end = Offset(xr.toFloat(), y.toFloat())
                )
                drawableItem.lines.add(Line(xl = xl, xr = xr, y = y))
            }
        }
    }
}
