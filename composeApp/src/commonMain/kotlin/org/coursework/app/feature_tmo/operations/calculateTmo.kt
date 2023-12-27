package org.coursework.app.feature_tmo.operations

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import org.coursework.app.feature_core.data.DrawableItem
import org.coursework.app.feature_operations.operations.getMaximumY
import org.coursework.app.feature_tmo.data.BL
import org.coursework.app.feature_tmo.data.Line
import org.coursework.app.feature_tmo.data.TmoType

fun calculateTmo(
    tmoType: TmoType,
    figureA: DrawableItem,
    figureB: DrawableItem,
    drawableItems: SnapshotStateList<DrawableItem>,
    onResult: (SnapshotStateList<DrawableItem>) -> Unit
) {
    val newFigureLines = mutableListOf<Line>()

    val SetQ = when (tmoType) {
        TmoType.MERGE -> shortArrayOf(1, 3)
        TmoType.DIFFERENCE -> shortArrayOf(2, 2)
    }

    val figureLinesA = calculateLines(figureA)
    val figureLinesB = calculateLines(figureB)

    val YallMin: Int = getMinimumYBothFigure(figureLinesA, figureLinesB)
    val YallMax: Int = getMaximumYBothFigure(figureLinesA, figureLinesB)
    for (j in YallMin until YallMax) {
        val XaLine = filterLinesByY(figureLinesA, j)
        val XbLine = filterLinesByY(figureLinesB, j)
        val M = buildMList(XaLine, XbLine)
        M.sortBy { it.x }
        var Q = 0
        var Qnew: Int
        val Xrl = mutableListOf<Int>()
        val Xrr = mutableListOf<Int>()

        M.forEach { mItem ->
            val x = mItem.x
            Qnew = Q + mItem.dQ
            //не принадлежит и принадлежит
            if ((Q < SetQ[0] || Q > SetQ[1]) && (Qnew >= SetQ[0] && Qnew <= SetQ[1])) {
                Xrl.add(x)
            }

            if ((Q >= SetQ[0] && Q <= SetQ[1]) && (Qnew < SetQ[0] || Qnew > SetQ[1])) {
                Xrr.add(x)
            }

            Q = Qnew
        }
        for (i in Xrl.indices) {
            newFigureLines.add(Line(Xrl[i], Xrr[i], j))
        }
    }
    val newDrawableItem = DrawableItem(
        offsetList = calculateOffsets(newFigureLines),
        color = figureA.color,
        tmoWasMake = true
    )
    drawableItems.remove(figureA)
    drawableItems.remove(figureB)
    drawableItems.add(newDrawableItem)
    onResult(drawableItems)
}

fun calculateOffsets(newFigureLines: MutableList<Line>): MutableList<Offset> {
    val resultOffsets = mutableListOf<Offset>()

    for (line in newFigureLines) {
        resultOffsets.add(Offset(line.xl.toFloat(), line.y.toFloat()))
        resultOffsets.add(Offset(line.xr.toFloat(), line.y.toFloat()))
    }

    return resultOffsets
}

fun calculateLines(drawableItem: DrawableItem): List<Line> {
    val lines = mutableListOf<Line>()
    if (drawableItem.tmoWasMake == true) {
        val offsets = drawableItem.offsetList
        for (i in offsets.indices step 2) {
            val xl = offsets[i].x
            val xr = if (i + 1 < offsets.size) offsets[i + 1].x else xl
            lines.add(Line(xl = xl.toInt(), xr = xr.toInt(), y = offsets[i].y.toInt()))
        }
        return lines
    } else {
        val offsetList = drawableItem.offsetList
        val minY = getMinimumY(offsetList)
        val maxY = getMaximumY(offsetList)
        val xb = mutableListOf<Float>()

        for (y in minY..maxY) {
            xb.clear()

            for (i in offsetList.indices) {
                val k = if (i < offsetList.size - 1) i + 1 else 0

                if ((offsetList[i].y > y && offsetList[k].y <= y) || (offsetList[k].y > y && offsetList[i].y <= y)) {
                    val x =
                        offsetList[i].x + (y - offsetList[i].y) / (offsetList[k].y - offsetList[i].y) * (offsetList[k].x - offsetList[i].x)
                    xb.add(x)
                }
            }

            xb.sort()

            for (i in xb.indices step 2) {
                val xl = xb[i].toInt()
                val xr = if (i + 1 < xb.size) xb[i + 1].toInt() else xl
                lines.add(Line(xl = xl, xr = xr, y = y))
            }
        }
        return lines
    }
}

fun getMinimumY(offsetList: MutableList<Offset>): Int {
    return offsetList.minOf { it.y }.toInt()
}


fun buildMList(xaLines: List<Line>, xbLines: List<Line>): MutableList<BL> {
    val M = mutableListOf<BL>()

    xaLines.forEach { xaLine ->
        M.add(BL(xaLine.xl, 2))
        M.add(BL(xaLine.xr, -2))
    }

    xbLines.forEach { xbLine ->
        M.add(BL(xbLine.xl, 1))
        M.add(BL(xbLine.xr, -1))
    }

    return M
}

fun getMinimumYBothFigure(lines1: List<Line>, lines2: List<Line>): Int {
    var minY = Int.MAX_VALUE

    for (line in lines1) {
        if (line.y < minY) {
            minY = line.y
        }
    }

    for (line in lines2) {
        if (line.y < minY) {
            minY = line.y
        }
    }

    return minY
}

fun getMaximumYBothFigure(lines1: List<Line>, lines2: List<Line>): Int {
    var maxY = Int.MIN_VALUE

    for (line in lines1) {
        if (line.y > maxY) {
            maxY = line.y
        }
    }

    for (line in lines2) {
        if (line.y > maxY) {
            maxY = line.y
        }
    }

    return maxY
}

fun filterLinesByY(lines: List<Line>, y: Int): List<Line> {
    val filteredLines = mutableListOf<Line>()

    for (line in lines) {
        if (line.y == y) {
            filteredLines.add(line)
        }
    }

    return filteredLines
}
