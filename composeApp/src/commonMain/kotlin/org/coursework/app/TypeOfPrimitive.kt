package org.coursework.app

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

enum class TypeOfPrimitive {
    FLAG,
    RECTANGULARTRIANGLE
}

fun flag(x: Float, y: Float): List<Offset> {
    val flagWidth = 100.dp.value
    val flagHeight = 75.dp.value
    return listOf(
        Offset(0f + x, 0f + y),
        Offset(flagWidth + x, 0f + y),
        Offset(flagWidth / 1.5f + x, flagHeight / 2f + y),
        Offset(flagWidth + x, flagHeight + y),
        Offset(0f + x, flagHeight + y)
    )
}

fun rectangularTriangle(x: Float, y: Float): List<Offset> {
    val sizeOfTriangle = 75.dp.value
    return listOf(
        Offset(0f + x, 0f + y),
        Offset(sizeOfTriangle + x, 0f + y),
        Offset(0f + x, sizeOfTriangle + y)
    )
}