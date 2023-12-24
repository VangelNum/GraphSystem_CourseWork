package org.coursework.app.feature_core

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.coursework.app.feature_core.data.ColorWithName

@Composable
fun getColorList(): List<ColorWithName> {
    return listOf(
        ColorWithName(Color.Black, "Черный"),
        ColorWithName(Color.Red, "Красный"),
        ColorWithName(Color.Green, "Зелёный"),
        ColorWithName(Color.Yellow, "Жёлтый"),
        ColorWithName(Color.Blue, "Синий"),
    )
}
