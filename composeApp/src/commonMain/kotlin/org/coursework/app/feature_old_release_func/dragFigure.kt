package org.coursework.app.feature_old_release_func

import androidx.compose.ui.geometry.Offset
import org.coursework.app.feature_core.data.DrawableItem

fun dragFigure(
    draggedFigure: DrawableItem,
    dx: Float,
    dy: Float,
    onSelectedFigure: (DrawableItem) -> Unit
) {
    val translationMatrix = arrayOf(
        floatArrayOf(1.0f, 0.0f, dx),
        floatArrayOf(0.0f, 1.0f, dy),
        floatArrayOf(0.0f, 0.0f, 1.0f)
    )
    draggedFigure.offsetList = draggedFigure.offsetList.map { offset ->
        val offsetVector = floatArrayOf(offset.x, offset.y, 1.0f)
        val result = FloatArray(3)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                result[i] += translationMatrix[i][j] * offsetVector[j]
            }
        }
        Offset(result[0], result[1])
    }.toMutableList()
    onSelectedFigure(draggedFigure)
}