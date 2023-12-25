package org.coursework.app.feature_default_primitive.operations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

fun calculateRectangularTrianglePoints(x: Float, y: Float): MutableList<Offset> {
    val sizeOfTriangle = 75.dp.value
    return mutableListOf(
        Offset(0f + x, 0f + y),
        Offset(sizeOfTriangle + x, 0f + y),
        Offset(0f + x, sizeOfTriangle + y)
    )
}