package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset

fun mirrorPointsSelectedCenter(points: List<Offset>, center: Offset): List<Offset> {
    return points.map { Offset(2 * center.x - it.x, it.y) }
}