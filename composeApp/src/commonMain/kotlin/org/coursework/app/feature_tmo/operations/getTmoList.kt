package org.coursework.app.feature_tmo.operations

import androidx.compose.runtime.Composable
import org.coursework.app.feature_tmo.data.TmoType
import org.coursework.app.feature_tmo.data.TmoWithName

@Composable
fun getTmoList(): List<TmoWithName> {
    return listOf(
        TmoWithName(TmoType.MERGE, "Объединение"),
        TmoWithName(TmoType.DIFFERENCE, "Разность A/B")
    )
}