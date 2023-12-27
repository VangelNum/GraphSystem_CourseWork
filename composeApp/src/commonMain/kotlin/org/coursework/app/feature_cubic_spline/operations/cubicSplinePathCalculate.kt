package org.coursework.app.feature_cubic_spline.operations

import androidx.compose.ui.geometry.Offset

fun cubicSplinePathCalculate(
    selectedPoints: List<Offset>
): MutableList<Offset> {
    val resultPoints = mutableListOf<Offset>()
    // Проверка, что у нас достаточно точек для построения кубического сплайна
    if (selectedPoints.size >= 4) {

        // Вычисление векторов для начальных условий
        val vector1 = Offset(
            4 * (selectedPoints[1].x - selectedPoints[0].x),
            4 * (selectedPoints[1].y - selectedPoints[0].y)
        )
        val vector2 = Offset(
            4 * (selectedPoints[3].x - selectedPoints[2].x),
            4 * (selectedPoints[3].y - selectedPoints[2].y)
        )

        // Массив для хранения коэффициентов полиномов
        val l = arrayOf(Offset(0f, 0f), Offset(0f, 0f), Offset(0f, 0f), Offset(0f, 0f))

        // Вычисление коэффициентов
        l[0] = Offset(
            2 * selectedPoints[0].x - 2 * selectedPoints[2].x + vector1.x + vector2.x,
            2 * selectedPoints[0].y - 2 * selectedPoints[2].y + vector1.y + vector2.y
        )

        l[1] = Offset(
            -3 * selectedPoints[0].x + 3 * selectedPoints[2].x - 2 * vector1.x - vector2.x,
            -3 * selectedPoints[0].y + 3 * selectedPoints[2].y - 2 * vector1.y - vector2.y
        )

        l[2] = vector1
        l[3] = Offset(selectedPoints[0].x, selectedPoints[0].y)

        // Параметр t, изменяющийся от 0 до 1
        var t = 0.0
        // Шаг для параметра t
        val dt = 0.04

        var pt: Offset

        // Вычисление точек сплайна и добавление их в список результатов
        while (t < 1 + dt / 2) {
            val xt = ((l[0].x * t + l[1].x) * t + l[2].x) * t + l[3].x
            val yt = ((l[0].y * t + l[1].y) * t + l[2].y) * t + l[3].y
            pt = Offset(xt.toFloat(), yt.toFloat())
            resultPoints.add(pt)
            t += dt
        }
    }
    return resultPoints.toMutableList()
}