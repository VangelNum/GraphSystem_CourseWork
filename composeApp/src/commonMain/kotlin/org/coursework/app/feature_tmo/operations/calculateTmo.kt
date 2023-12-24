package org.coursework.app.feature_tmo.operations

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import org.coursework.app.feature_core.data.DrawableItem
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

    val YallMin: Int = getMinimumYBothFigure(figureA.lines, figureB.lines)
    val YallMax: Int = getMaximumYBothFigure(figureA.lines, figureB.lines)
    for (j in YallMin until YallMax) {
        val XaLine = filterLinesByY(figureA.lines, j)
        val XbLine = filterLinesByY(figureB.lines, j)
        val M = buildMList(XaLine, XbLine)
        M.sortBy { it.x }
        var Q = 0
        var Qnew: Int
        val Xrl = mutableListOf<Int>()
        val Xrr = mutableListOf<Int>()

        M.forEach { mItem ->
            val x = mItem.x
            Qnew = Q + mItem.dQ

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
        lines = newFigureLines,
        tmoWasMake = true
    )
    drawableItems.remove(figureA)
    drawableItems.remove(figureB)
    drawableItems.add(newDrawableItem)
    onResult(drawableItems)
}

fun calculateOffsets(newFigureLines: MutableList<Line>): List<Offset> {
    val resultOffsets = mutableListOf<Offset>()

    for (line in newFigureLines) {
        resultOffsets.add(Offset(line.xl.toFloat(), line.y.toFloat()))
        resultOffsets.add(Offset(line.xr.toFloat(), line.y.toFloat()))
    }

    return resultOffsets
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
