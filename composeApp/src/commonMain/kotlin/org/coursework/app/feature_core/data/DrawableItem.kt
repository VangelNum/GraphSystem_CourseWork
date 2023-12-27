package org.coursework.app.feature_core.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

interface Draggable {
    fun dragFigure(dx: Float, dy: Float, onSelectedFigure: (DrawableItem) -> Unit)
}

interface Mirroring {
    fun mirrorPointsCenterFigure(center: Offset)
    fun mirrorPointsVertical(verticalAxisX: Float)
}

interface Rotatable {
    fun rotateFigure(center: Offset, rotationAngle: Float)
}

class DrawableItem(
    var offsetList: MutableList<Offset>,
    val color: Color,
    val isCubicSpline: Boolean = false,
    var tmoWasMake: Boolean? = false
) : Draggable, Mirroring, Rotatable {

    override fun dragFigure(dx: Float, dy: Float, onSelectedFigure: (DrawableItem) -> Unit) {
        val translationMatrix = arrayOf(
            floatArrayOf(1.0f, 0.0f, dx),
            floatArrayOf(0.0f, 1.0f, dy),
            floatArrayOf(0.0f, 0.0f, 1.0f)
        )
        offsetList = multiplyMatrix(offsetList, translationMatrix)
        onSelectedFigure(this)
    }

    override fun mirrorPointsCenterFigure(center: Offset) {
        val transformationMatrix = arrayOf(
            floatArrayOf(-1f, 0f, 2 * center.x),
            floatArrayOf(0f, -1f, 2 * center.y),
            floatArrayOf(0f, 0f, 1f)
        )
        offsetList = multiplyMatrix(offsetList, transformationMatrix)
    }

    override fun mirrorPointsVertical(verticalAxisX: Float) {
        val transformationMatrix = arrayOf(
            floatArrayOf(-1f, 0f, 2 * verticalAxisX),
            floatArrayOf(0f, 1f, 0f),
            floatArrayOf(0f, 0f, 1f)
        )
        offsetList = multiplyMatrix(offsetList, transformationMatrix)
    }

    override fun rotateFigure(center: Offset, rotationAngle: Float) {
        val angleRadians = rotationAngle / 180 * PI
        val cosTheta = cos(angleRadians).toFloat()
        val sinTheta = sin(angleRadians).toFloat()

        val rotationMatrix = arrayOf(
            floatArrayOf(cosTheta, -sinTheta, 0f),
            floatArrayOf(sinTheta, cosTheta, 0f),
            floatArrayOf(0f, 0f, 1f)
        )

        offsetList = multiplyMatrix(offsetList.map {
            Offset(it.x - center.x, it.y - center.y)
        }, rotationMatrix).map {
            Offset(it.x + center.x, it.y + center.y)
        }.toMutableList()
    }

    private fun multiplyMatrix(points: List<Offset>, matrix: Array<FloatArray>): MutableList<Offset> {
        return points.map { point ->
            val result = FloatArray(3)
            for (i in 0 until 3) {
                result[i] = matrix[i][0] * point.x + matrix[i][1] * point.y + matrix[i][2]
            }
            Offset(result[0], result[1])
        }.toMutableList()
    }
}