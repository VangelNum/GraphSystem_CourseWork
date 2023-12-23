package org.coursework.app

data class TmoWithName (
    val tmoType: TmoType,
    val name: String
)

enum class TmoType {
    MERGE,
    DIFFERENCE
}