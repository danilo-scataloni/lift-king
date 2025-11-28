package com.daniloscataloni.liftking.entities

data class Workout (
    val id: Int,
    val name: String,
    val exercises: List<Exercise>
)