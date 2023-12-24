package org.coursework.app

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DrawableItem(
    var offsetList: List<Offset>,
    val color: Color,
    val isCubicSpline: Boolean = false,
    var rotationAngle: Float = 0f,
    var rotationCenter: Offset? = null,
    val lines: MutableList<Line> = mutableListOf(),
    var tmoWasMake: Boolean? = false
)