package com.daniloscataloni.liftking.data.mappers

import com.daniloscataloni.liftking.data.entities.ExerciseEntity
import com.daniloscataloni.liftking.data.entities.ExerciseLogEntity
import com.daniloscataloni.liftking.data.entities.PeriodizationEntity
import com.daniloscataloni.liftking.data.entities.SetLogEntity
import com.daniloscataloni.liftking.data.entities.TrainingSessionEntity
import com.daniloscataloni.liftking.data.entities.WorkoutEntity
import com.daniloscataloni.liftking.data.entities.WorkoutExerciseEntity
import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.domain.models.ExerciseLog
import com.daniloscataloni.liftking.domain.models.Periodization
import com.daniloscataloni.liftking.domain.models.SetLog
import com.daniloscataloni.liftking.domain.models.TrainingSession
import com.daniloscataloni.liftking.domain.models.Workout
import com.daniloscataloni.liftking.domain.models.WorkoutExercise

fun ExerciseEntity.toDomain(): Exercise {
    return Exercise(
        id = this.id,
        description = this.description,
        primaryMuscleGroup = this.primaryMuscleGroup,
        secondaryMuscleGroups = this.secondaryMuscleGroups
    )
}

fun Exercise.toEntity(): ExerciseEntity {
    return ExerciseEntity(
        id = this.id,
        description = this.description,
        primaryMuscleGroup = this.primaryMuscleGroup,
        secondaryMuscleGroups = this.secondaryMuscleGroups
    )
}

fun PeriodizationEntity.toDomain(): Periodization {
    return Periodization(
        id = this.id,
        name = this.name,
        isActive = this.isActive,
        isArchived = this.isArchived,
        createdAt = this.createdAt
    )
}

fun Periodization.toEntity(): PeriodizationEntity {
    return PeriodizationEntity(
        id = this.id,
        name = this.name,
        isActive = this.isActive,
        isArchived = this.isArchived,
        createdAt = this.createdAt
    )
}

fun WorkoutEntity.toDomain(): Workout {
    return Workout(
        id = this.id,
        periodizationId = this.periodizationId,
        name = this.name,
        order = this.order
    )
}

fun Workout.toEntity(): WorkoutEntity {
    return WorkoutEntity(
        id = this.id,
        periodizationId = this.periodizationId,
        name = this.name,
        order = this.order
    )
}

fun WorkoutExerciseEntity.toDomain(): WorkoutExercise {
    return WorkoutExercise(
        id = this.id,
        workoutId = this.workoutId,
        exerciseId = this.exerciseId,
        order = this.order,
        targetSets = this.targetSets
    )
}

fun WorkoutExercise.toEntity(): WorkoutExerciseEntity {
    return WorkoutExerciseEntity(
        id = this.id,
        workoutId = this.workoutId,
        exerciseId = this.exerciseId,
        order = this.order,
        targetSets = this.targetSets
    )
}

fun TrainingSessionEntity.toDomain(): TrainingSession {
    return TrainingSession(
        id = this.id,
        workoutId = this.workoutId,
        date = this.date,
        notes = this.notes,
        isCompleted = this.isCompleted
    )
}

fun TrainingSession.toEntity(): TrainingSessionEntity {
    return TrainingSessionEntity(
        id = this.id,
        workoutId = this.workoutId,
        date = this.date,
        notes = this.notes,
        isCompleted = this.isCompleted
    )
}

fun ExerciseLogEntity.toDomain(): ExerciseLog {
    return ExerciseLog(
        id = this.id,
        sessionId = this.sessionId,
        exerciseId = this.exerciseId,
        notes = this.notes
    )
}

fun ExerciseLog.toEntity(): ExerciseLogEntity {
    return ExerciseLogEntity(
        id = this.id,
        sessionId = this.sessionId,
        exerciseId = this.exerciseId,
        notes = this.notes
    )
}

fun SetLogEntity.toDomain(): SetLog {
    return SetLog(
        id = this.id,
        exerciseLogId = this.exerciseLogId,
        setNumber = this.setNumber,
        weight = this.weight,
        reps = this.reps,
        rir = this.rir
    )
}

fun SetLog.toEntity(): SetLogEntity {
    return SetLogEntity(
        id = this.id,
        exerciseLogId = this.exerciseLogId,
        setNumber = this.setNumber,
        weight = this.weight,
        reps = this.reps,
        rir = this.rir
    )
}
