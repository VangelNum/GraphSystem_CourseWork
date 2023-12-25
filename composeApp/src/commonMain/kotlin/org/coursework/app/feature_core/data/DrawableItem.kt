package org.coursework.app.feature_core.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import org.coursework.app.feature_tmo.data.Line

data class DrawableItem(
    var offsetList: MutableList<Offset>,
    val color: Color,
    val isCubicSpline: Boolean = false,
    var rotationAngle: Float = 0f,
    var rotationCenter: Offset? = null,
    val lines: MutableList<Line> = mutableListOf(),
    var tmoWasMake: Boolean? = false
)