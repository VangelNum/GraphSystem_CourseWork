package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun rotateFigure(points: List<Offset>, center: Offset, angleDegrees: Float): MutableList<Offset> {
    val resultPoints = mutableListOf<Offset>()

    val angleRadians = angleDegrees / 180 * PI
    val cosTheta = cos(angleRadians)
    val sinTheta = sin(angleRadians)

    for (point in points) {
        val translatedX = point.x - center.x
        val translatedY = point.y - center.y

        val rotatedX = translatedX * cosTheta - translatedY * sinTheta
        val rotatedY = translatedX * sinTheta + translatedY * cosTheta

        resultPoints.add(Offset(rotatedX.toFloat() + center.x, rotatedY.toFloat() + center.y))
    }

    return resultPoints.toMutableList()
}