package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset

fun mirrorPointsVertical(points: List<Offset>, verticalAxisX: Float): List<Offset> {
    return points.map { Offset(2 * verticalAxisX - it.x, it.y) }
}