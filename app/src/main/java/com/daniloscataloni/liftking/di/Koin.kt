package com.daniloscataloni.liftking.di

import androidx.room.Room
import com.daniloscataloni.liftking.data.LiftKingDatabase
import com.daniloscataloni.liftking.repositories.ExerciseRepository
import com.daniloscataloni.liftking.repositories.IExerciseRepository
import com.daniloscataloni.liftking.repositories.IPeriodizationRepository
import com.daniloscataloni.liftking.repositories.ITrainingRepository
import com.daniloscataloni.liftking.repositories.IWorkoutRepository
import com.daniloscataloni.liftking.repositories.PeriodizationRepository
import com.daniloscataloni.liftking.repositories.TrainingRepository
import com.daniloscataloni.liftking.repositories.WorkoutRepository
import com.daniloscataloni.liftking.ui.viewmodels.ExerciseCreationViewModel
import com.daniloscataloni.liftking.ui.viewmodels.ExerciseListViewModel
import com.daniloscataloni.liftking.ui.viewmodels.PeriodizationViewModel
import com.daniloscataloni.liftking.ui.viewmodels.TrainingViewModel
import com.daniloscataloni.liftking.ui.viewmodels.WorkoutListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {

    // ========== ViewModels ==========
    viewModelOf(::ExerciseCreationViewModel)
    viewModelOf(::ExerciseListViewModel)
    viewModelOf(::PeriodizationViewModel)
    viewModelOf(::TrainingViewModel)
    viewModelOf(::WorkoutListViewModel)

    // ========== Repositories ==========
    singleOf(::ExerciseRepository) { bind<IExerciseRepository>() }
    singleOf(::PeriodizationRepository) { bind<IPeriodizationRepository>() }
    singleOf(::WorkoutRepository) { bind<IWorkoutRepository>() }
    singleOf(::TrainingRepository) { bind<ITrainingRepository>() }

    // ========== DAOs ==========
    single { get<LiftKingDatabase>().exerciseDao() }
    single { get<LiftKingDatabase>().periodizationDao() }
    single { get<LiftKingDatabase>().workoutDao() }
    single { get<LiftKingDatabase>().workoutExerciseDao() }
    single { get<LiftKingDatabase>().trainingSessionDao() }
    single { get<LiftKingDatabase>().exerciseLogDao() }
    single { get<LiftKingDatabase>().setLogDao() }

    // ========== Database ==========
    single {
        Room.databaseBuilder(
            androidContext(),
            LiftKingDatabase::class.java,
            "lift-king-database"
        )
            .addMigrations(LiftKingDatabase.MIGRATION_1_2)
            .build()
    }
}
