package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset

fun getMaximumY(offsetList: MutableList<Offset>): Int {
    return offsetList.maxOf { it.y }.toInt()
}