package com.daniloscataloni.liftking.entities

data class Exercise (
    val id: Int,
    val description: String,
    val muscleGroup: List<MuscleGroup>
)

enum class MuscleGroup() {
    CHEST,
    BACK,
    QUADS,
    HAMSTRINGS,
    BICEPS,
    TRICEPS,
    SHOULDERS,
    CALVES,
    ABS,
    FOREARMS
}