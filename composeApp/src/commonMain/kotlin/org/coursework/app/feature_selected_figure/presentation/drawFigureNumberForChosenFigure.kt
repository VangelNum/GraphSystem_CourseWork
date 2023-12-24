package org.coursework.app.feature_selected_figure.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import org.coursework.app.feature_core.data.DrawableItem

fun DrawScope.drawFigureNumberForChosenFigure(
    selectedFigure: DrawableItem?,
    textMeasurer: TextMeasurer,
    drawableItems: List<DrawableItem>
) {
    selectedFigure?.let {
        val text = "${drawableItems.indexOf(selectedFigure) + 1}"

        drawText(
            textMeasurer = textMeasurer,
            text = text,
            topLeft = center,
            style = TextStyle(
                color = Color.Red,
                fontSize = 12.sp
            )
        )
    }
}
