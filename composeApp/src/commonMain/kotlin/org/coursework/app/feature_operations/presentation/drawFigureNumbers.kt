package org.coursework.app.feature_operations.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import org.coursework.app.feature_core.data.DrawableItem
import org.coursework.app.feature_operations.operations.getCenterOfFigure

fun DrawScope.drawFigureNumbers(drawableItems: List<DrawableItem>, textMeasurer: TextMeasurer) {
    drawableItems.forEachIndexed { index, drawableItem ->
        val text = (index + 1).toString()
        val center = getCenterOfFigure(drawableItem)
        try {
            drawText(
                textMeasurer = textMeasurer, text = text, topLeft = center, style = TextStyle(
                    color = Color.Magenta,
                    fontSize = 24.sp
                )
            )
        } catch (e: Exception) {
            println("Experimental API ${e.message}")
        }
    }
}
