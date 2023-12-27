package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset

fun getMinimumY(offsetList: MutableList<Offset>): Int {
    return offsetList.minOf { it.y }.toInt()
}
