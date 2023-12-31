package org.coursework.app.feature_current_figure.data

import androidx.compose.ui.geometry.Offset

data class CurrentFigure(
    var offsetList: MutableList<Offset>,
    var isCubicSpline: Boolean,
    val tempListForLinesCubicSpline: MutableList<Offset>
)
