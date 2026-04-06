package com.daniloscataloni.liftking.domain.models

enum class WeightUnit(val displayLabel: String, val shortLabel: String) {
    KG(displayLabel = "Quilogramas", shortLabel = "kg"),
    LIBRAS(displayLabel = "Libras", shortLabel = "lb"),
    TIJOLINHOS(displayLabel = "Tijolinhos", shortLabel = "tij")
}
