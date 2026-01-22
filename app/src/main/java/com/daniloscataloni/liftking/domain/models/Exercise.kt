package com.daniloscataloni.liftking.domain.models

data class Exercise (
    val id: Int,
    val description: String,
    val primaryMuscleGroup: MuscleGroup,
    val secondaryMuscleGroups: MuscleGroup?
)

enum class MuscleGroup {
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
