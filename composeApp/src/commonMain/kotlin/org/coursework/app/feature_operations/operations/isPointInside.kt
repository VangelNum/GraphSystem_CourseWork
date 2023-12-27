package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset
import org.coursework.app.feature_core.data.DrawableItem

fun isPointInside(point: Offset, drawableItem: DrawableItem): Boolean {
    val offsetList = drawableItem.offsetList
    if (offsetList.size == 2) {
        val segmentStart = offsetList[0]
        val segmentEnd = offsetList[1]

        val minX = minOf(segmentStart.x, segmentEnd.x)
        val maxX = maxOf(segmentStart.x, segmentEnd.x)
        val minY = minOf(segmentStart.y, segmentEnd.y)
        val maxY = maxOf(segmentStart.y, segmentEnd.y)

        return point.x in minX..maxX && point.y in minY..maxY
    }

    var isInside = false

    // Проходим по вершинам многоугольника
    for (i in 0 until offsetList.size) {
        // Текущая вершина
        val current = offsetList[i]

        // Следующая вершина (замыкание списка)
        val next = offsetList[(i + 1) % offsetList.size]

        // Проверка, лежит ли точка выше или ниже текущей и следующей вершин многоугольника
        if ((current.y > point.y) != (next.y > point.y) &&
            // Проверка, лежит ли точка левее отрезка между текущей и следующей вершинами
            (point.x < (next.x - current.x) * (point.y - current.y) / (next.y - current.y) + current.x)
        ) {
            // Если условия выполняются, меняем состояние isInside
            isInside = !isInside
        }

        // Выводим текущее состояние isInside (true, если точка внутри, false в противном случае)
        println(isInside)
    }
    return isInside

}