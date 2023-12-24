package org.coursework.app.feature_operations.operations

import androidx.compose.runtime.Composable
import org.coursework.app.feature_operations.data.OperationType
import org.coursework.app.feature_operations.data.OperationWithName

@Composable
fun getOperationList(): List<OperationWithName> {
    return listOf(
        OperationWithName(OperationType.DrawPrimitive, "Рисование примитивов"),
        OperationWithName(OperationType.PastPrimitive, "Вставка примитивов"),
        OperationWithName(OperationType.DrawCubeSpline, "Рисование кубического сплайна"),
        OperationWithName(OperationType.WorkWithObject, "Работа с объектами"),
    )
}