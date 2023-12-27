package org.coursework.app.feature_old_release_func

import androidx.compose.ui.geometry.Offset

fun mirrorPointsVertical(points: List<Offset>, verticalAxisX: Float): MutableList<Offset> {
    //return points.map { Offset(2 * verticalAxisX - it.x, it.y) }.toMutableList()
    val transformationMatrix = arrayOf(
        floatArrayOf(-1f, 0f, 2 * verticalAxisX),
        floatArrayOf(0f, 1f, 0f),
        floatArrayOf(0f, 0f, 1f)
    )

    return points.map { point ->
        val result = FloatArray(3)
        for (i in 0 until 3) {
            result[i] = transformationMatrix[i][0] * point.x + transformationMatrix[i][1] * point.y + transformationMatrix[i][2]
        }
        Offset(result[0], result[1])
    }.toMutableList()
}