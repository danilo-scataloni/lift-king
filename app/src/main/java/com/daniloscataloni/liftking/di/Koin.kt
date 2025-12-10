package com.daniloscataloni.liftking.di

import androidx.room.Room
import com.daniloscataloni.liftking.data.LiftKingDatabase
import com.daniloscataloni.liftking.repositories.ExerciseRepository
import com.daniloscataloni.liftking.repositories.IExerciseRepository
import com.daniloscataloni.liftking.viewmodels.ExerciseCreationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::ExerciseCreationViewModel)

    single {
        Room.databaseBuilder(
            androidContext(),
            LiftKingDatabase::class.java, "lift-king-database"
        ).build()
    }

    single { get<LiftKingDatabase>().exerciseDao() }

    singleOf(::ExerciseRepository) { bind<IExerciseRepository>() }
}