package org.coursework.app.feature_default_primitive.operations

import androidx.compose.runtime.Composable
import org.coursework.app.feature_default_primitive.data.PrimitiveTypeWithName
import org.coursework.app.feature_default_primitive.data.TypeOfPrimitive

@Composable
fun getPrimitiveList(): List<PrimitiveTypeWithName> {
    return listOf(
        PrimitiveTypeWithName(TypeOfPrimitive.FLAG, "Флаг"),
        PrimitiveTypeWithName(TypeOfPrimitive.RECTANGULARTRIANGLE, "Прямоугольный треугольник")
    )
}