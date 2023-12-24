package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset

fun getMaximumY(offsetList: List<Offset>): Int {
    return offsetList.maxOf { it.y }.toInt()
}