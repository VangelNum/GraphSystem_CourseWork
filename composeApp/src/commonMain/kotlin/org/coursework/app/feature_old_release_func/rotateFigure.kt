package org.coursework.app.feature_old_release_func

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun rotateFigure(points: List<Offset>, center: Offset, angleDegrees: Float): MutableList<Offset> {
    val resultPoints = mutableListOf<Offset>()

    val angleRadians = angleDegrees / 180 * PI
    val cosTheta = cos(angleRadians).toFloat()
    val sinTheta = sin(angleRadians).toFloat()

    val rotationMatrix = arrayOf(
        floatArrayOf(cosTheta, -sinTheta, 0f),
        floatArrayOf(sinTheta, cosTheta, 0f),
        floatArrayOf(0f, 0f, 1f)
    )

    for (point in points) {
        val translatedX = point.x - center.x
        val translatedY = point.y - center.y

        val rotatedX = rotationMatrix[0][0] * translatedX + rotationMatrix[0][1] * translatedY
        val rotatedY = rotationMatrix[1][0] * translatedX + rotationMatrix[1][1] * translatedY

        resultPoints.add(Offset(rotatedX + center.x, rotatedY + center.y))
    }

    return resultPoints.toMutableList()
}
