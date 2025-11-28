package com.daniloscataloni.liftking.entities

data class Periodization(
    val id: Int,
    val name: String,
    val workouts: List<Workout>
)