package org.coursework.app.feature_default_primitive.operations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

fun calculateFlagPoints(x: Float, y: Float): MutableList<Offset> {
    val flagWidth = 100.dp.value
    val flagHeight = 75.dp.value
    return mutableListOf(
        Offset(0f + x, 0f + y),
        Offset(flagWidth + x, 0f + y),
        Offset(flagWidth / 1.5f + x, flagHeight / 2f + y),
        Offset(flagWidth + x, flagHeight + y),
        Offset(0f + x, flagHeight + y)
    )
}