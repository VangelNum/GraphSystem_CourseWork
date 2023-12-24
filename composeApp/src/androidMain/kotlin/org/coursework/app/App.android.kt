package org.coursework.app

import android.app.Application
import android.content.Intent
import android.graphics.Point
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt


class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
            val points = arrayOf(
                Offset(50F, 50F),
                Offset(150F, 200F),
                Offset(250F, 50F),
                Offset(350F, 200F)
            )
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                val pv1 = Offset(4 * (points[1].x - points[0].x), 4 * (points[1].y - points[0].y))
                val pv2 = Offset(4 * (points[3].x - points[2].x), 4 * (points[3].y - points[2].y))

                val l = Array(4) { Offset(0f, 0f) }

                l[0] = Offset(
                    2 * points[0].x - 2 * points[2].x + pv1.x + pv2.x,
                    2 * points[0].y - 2 * points[2].y + pv1.y + pv2.y
                )

                l[1] = Offset(
                    -3 * points[0].x + 3 * points[2].x - 2 * pv1.x - pv2.x,
                    -3 * points[0].y + 3 * points[2].y - 2 * pv1.y - pv2.y
                )

                l[2] = pv1
                l[3] = points[0]

                var t = 0.0
                val dt = 0.04

                var pt: Offset
                var ppred = l[3]

                while (t < 1 + dt / 2) {
                    val xt = ((l[0].x * t + l[1].x) * t + l[2].x) * t + l[3].x
                    val yt = ((l[0].y * t + l[1].y) * t + l[2].y) * t + l[3].y
                    pt = Offset(xt.toFloat(), yt.toFloat())

                    drawLine(Color.Yellow, ppred, pt)

                    ppred = pt
                    t += dt
                }

            })
        }
    }
}


@Composable
fun CubicSplineDrawer() {
    val points = arrayOf(
        Point(50, 50),
        Point(150, 200),
        Point(250, 50),
        Point(350, 200)
    )
//    var t = 0.0
//    Canvas(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        val dt = 0
//        var L = FloatArray(8) // Матрица вещественных коэффициентов
//        var xt: Float
//        var yt: Float
//        var Ppred = points[0]
//        val Pt = Point(points[0].x, points[0].y)
//
//        // Касательные векторы
//        val Pv1 = Point(4 * (points[1].x - points[0].x), 4 * (points[1].y - points[0].y))
//        val Pv2 = Point(4 * (points[3].x - points[2].x), 4 * (points[3].y - points[2].y))
//
//        // Коэффициенты полинома
//        L[0] = (2 * points[0].x - 2 * points[2].x + Pv1.x + Pv2.x).toFloat() // Ax
//        L[1] = (2 * points[0].y - 2 * points[2].y + Pv1.y + Pv2.y).toFloat() // Ay
//        L[2] = (-3 * points[0].x + 3 * points[2].x - 2 * Pv1.x - Pv2.x).toFloat() // Bx
//        L[3] = (-3 * points[0].y + 3 * points[2].y - 2 * Pv1.y - Pv2.y).toFloat() // By
//        L[4] = Pv1.x.toFloat() // Cx
//        L[5] = Pv1.y.toFloat() // Cy
//        L[6] = points[0].x.toFloat() // Dx
//        L[7] = points[0].y.toFloat() // Dy
//
//        while (t < 1 + dt / 2) {
//            xt = (((L[0] * t + L[2]) * t + L[4]) * t + L[6]).toFloat()
//            yt = (((L[1] * t + L[3]) * t + L[5]) * t + L[7]).toFloat()
//            Pt.x = xt.roundToInt()
//            Pt.y = yt.roundToInt()
//            drawLine(
//                start = Offset(Ppred.x.toFloat(), Ppred.y.toFloat()),
//                end = Offset(Pt.x.toFloat() + 1f, Pt.y.toFloat()+1f),
//                color = Color.Yellow,
//                strokeWidth = 3f
//            )
//            Ppred = Pt
//            t += dt
//        }
//    }
}


internal actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AndroidApp.INSTANCE.startActivity(intent)
}

internal actual fun getPlatformName(): String {
    return "Android"
}