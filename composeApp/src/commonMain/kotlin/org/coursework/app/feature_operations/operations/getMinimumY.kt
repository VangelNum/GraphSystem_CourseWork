package org.coursework.app.feature_operations.operations

import androidx.compose.ui.geometry.Offset

fun getMinimumY(offsetList: List<Offset>): Int {
    return offsetList.minOf { it.y }.toInt()
}