package org.coursework.app.feature_cubic_spline.operations

import androidx.compose.ui.geometry.Offset

fun cubicSplinePathCalculate(
    selectedPoints: List<Offset>
): List<Offset> {
    val resultPoints = mutableListOf<Offset>()
    if (selectedPoints.size >= 4) {
        val pv1 = Offset(
            4 * (selectedPoints[1].x - selectedPoints[0].x),
            4 * (selectedPoints[1].y - selectedPoints[0].y)
        )
        val pv2 = Offset(
            4 * (selectedPoints[3].x - selectedPoints[2].x),
            4 * (selectedPoints[3].y - selectedPoints[2].y)
        )

        val l = arrayOf(Offset(0f, 0f), Offset(0f, 0f), Offset(0f, 0f), Offset(0f, 0f))

        l[0] = Offset(
            2 * selectedPoints[0].x - 2 * selectedPoints[2].x + pv1.x + pv2.x,
            2 * selectedPoints[0].y - 2 * selectedPoints[2].y + pv1.y + pv2.y
        )

        l[1] = Offset(
            -3 * selectedPoints[0].x + 3 * selectedPoints[2].x - 2 * pv1.x - pv2.x,
            -3 * selectedPoints[0].y + 3 * selectedPoints[2].y - 2 * pv1.y - pv2.y
        )

        l[2] = pv1
        l[3] = Offset(selectedPoints[0].x, selectedPoints[0].y)

        var t = 0.0
        val dt = 0.04

        var pt: Offset
        var ppred = l[3]

        while (t < 1 + dt / 2) {
            val xt = ((l[0].x * t + l[1].x) * t + l[2].x) * t + l[3].x
            val yt = ((l[0].y * t + l[1].y) * t + l[2].y) * t + l[3].y
            pt = Offset(xt.toFloat(), yt.toFloat())

            resultPoints.add(pt)

            ppred = pt
            t += dt
        }
    }
    return resultPoints
}