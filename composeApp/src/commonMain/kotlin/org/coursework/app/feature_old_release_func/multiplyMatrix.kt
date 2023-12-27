package org.coursework.app.feature_old_release_func

import androidx.compose.ui.geometry.Offset

private fun multiplyMatrix(vector: FloatArray, matrix: Array<FloatArray>): Offset {
    val result = FloatArray(3)
    for (i in 0 until 3) {
        for (j in 0 until 3) {
            result[i] += matrix[i][j] * vector[j]
        }
    }
    return Offset(result[0], result[1])
}
