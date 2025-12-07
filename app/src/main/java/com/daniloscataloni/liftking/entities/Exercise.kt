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

fun MuscleGroup.toReadableString(): String {
    return when (this) {
        MuscleGroup.CHEST -> "Peito"
        MuscleGroup.BACK -> "Costas"
        MuscleGroup.QUADS -> "Quadríceps"
        MuscleGroup.HAMSTRINGS -> "Posterior de Coxa"
        MuscleGroup.BICEPS -> "Bíceps"
        MuscleGroup.TRICEPS -> "Tríceps"
        MuscleGroup.SHOULDERS -> "Ombro"
        MuscleGroup.CALVES -> "Panturrilha"
        MuscleGroup.ABS -> "Abdominal"
        MuscleGroup.FOREARMS -> "Antebraço"
    }
}