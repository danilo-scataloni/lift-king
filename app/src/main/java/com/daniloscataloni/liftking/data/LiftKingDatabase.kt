package com.daniloscataloni.liftking.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.daniloscataloni.liftking.data.daos.ExerciseDao
import com.daniloscataloni.liftking.data.entities.ExerciseEntity

@Database(
    entities = [
        ExerciseEntity::class,
    ],
    version = 1,
)
abstract class LiftKingDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
}