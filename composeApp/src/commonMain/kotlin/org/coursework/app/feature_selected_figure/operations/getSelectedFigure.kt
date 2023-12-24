package org.coursework.app.feature_selected_figure.operations

import androidx.compose.ui.geometry.Offset
import org.coursework.app.feature_core.data.DrawableItem
import org.coursework.app.feature_operations.operations.isPointInside

fun getSelectedFigure(position: Offset, figures: List<DrawableItem>): DrawableItem? {
    return figures.lastOrNull { drawableItem ->
        isPointInside(position, drawableItem)
    }
}