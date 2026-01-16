package com.daniloscataloni.liftking.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.daniloscataloni.liftking.data.daos.ExerciseDao
import com.daniloscataloni.liftking.data.daos.ExerciseLogDao
import com.daniloscataloni.liftking.data.daos.PeriodizationDao
import com.daniloscataloni.liftking.data.daos.SetLogDao
import com.daniloscataloni.liftking.data.daos.TrainingSessionDao
import com.daniloscataloni.liftking.data.daos.WorkoutDao
import com.daniloscataloni.liftking.data.daos.WorkoutExerciseDao
import com.daniloscataloni.liftking.data.entities.ExerciseEntity
import com.daniloscataloni.liftking.data.entities.ExerciseLogEntity
import com.daniloscataloni.liftking.data.entities.PeriodizationEntity
import com.daniloscataloni.liftking.data.entities.SetLogEntity
import com.daniloscataloni.liftking.data.entities.TrainingSessionEntity
import com.daniloscataloni.liftking.data.entities.WorkoutEntity
import com.daniloscataloni.liftking.data.entities.WorkoutExerciseEntity

/**
 * Banco de dados principal do LiftKing.
 *
 * Histórico de versões:
 * - v1: Apenas exercises
 * - v2: Adicionado periodizations, workouts, workout_exercises,
 *       training_sessions, exercise_logs, set_logs
 */
@Database(
    entities = [
        ExerciseEntity::class,
        PeriodizationEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        TrainingSessionEntity::class,
        ExerciseLogEntity::class,
        SetLogEntity::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class LiftKingDatabase : RoomDatabase() {

    // DAOs
    abstract fun exerciseDao(): ExerciseDao
    abstract fun periodizationDao(): PeriodizationDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun trainingSessionDao(): TrainingSessionDao
    abstract fun exerciseLogDao(): ExerciseLogDao
    abstract fun setLogDao(): SetLogDao

    companion object {
        /**
         * Migração da versão 1 para 2.
         * Cria todas as novas tabelas mantendo os dados existentes.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Tabela de periodizações
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS periodizations (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        isActive INTEGER NOT NULL DEFAULT 0,
                        isArchived INTEGER NOT NULL DEFAULT 0,
                        createdAt INTEGER NOT NULL
                    )
                """)

                // Tabela de workouts
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS workouts (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        periodizationId INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        `order` INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY (periodizationId) REFERENCES periodizations(id) ON DELETE CASCADE
                    )
                """)
                db.execSQL("CREATE INDEX IF NOT EXISTS index_workouts_periodizationId ON workouts(periodizationId)")

                // Tabela de workout_exercises
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS workout_exercises (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        workoutId INTEGER NOT NULL,
                        exerciseId INTEGER NOT NULL,
                        `order` INTEGER NOT NULL DEFAULT 0,
                        targetSets INTEGER,
                        FOREIGN KEY (workoutId) REFERENCES workouts(id) ON DELETE CASCADE,
                        FOREIGN KEY (exerciseId) REFERENCES exercises(id) ON DELETE RESTRICT
                    )
                """)
                db.execSQL("CREATE INDEX IF NOT EXISTS index_workout_exercises_workoutId ON workout_exercises(workoutId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_workout_exercises_exerciseId ON workout_exercises(exerciseId)")

                // Tabela de training_sessions
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS training_sessions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        workoutId INTEGER NOT NULL,
                        date INTEGER NOT NULL,
                        notes TEXT,
                        isCompleted INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY (workoutId) REFERENCES workouts(id) ON DELETE CASCADE
                    )
                """)
                db.execSQL("CREATE INDEX IF NOT EXISTS index_training_sessions_workoutId ON training_sessions(workoutId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_training_sessions_date ON training_sessions(date)")

                // Tabela de exercise_logs
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS exercise_logs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        sessionId INTEGER NOT NULL,
                        exerciseId INTEGER NOT NULL,
                        `order` INTEGER NOT NULL DEFAULT 0,
                        notes TEXT,
                        FOREIGN KEY (sessionId) REFERENCES training_sessions(id) ON DELETE CASCADE,
                        FOREIGN KEY (exerciseId) REFERENCES exercises(id) ON DELETE RESTRICT
                    )
                """)
                db.execSQL("CREATE INDEX IF NOT EXISTS index_exercise_logs_sessionId ON exercise_logs(sessionId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_exercise_logs_exerciseId ON exercise_logs(exerciseId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_exercise_logs_exerciseId_sessionId ON exercise_logs(exerciseId, sessionId)")

                // Tabela de set_logs
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS set_logs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        exerciseLogId INTEGER NOT NULL,
                        setNumber INTEGER NOT NULL,
                        weight REAL NOT NULL,
                        reps INTEGER NOT NULL,
                        rir INTEGER,
                        FOREIGN KEY (exerciseLogId) REFERENCES exercise_logs(id) ON DELETE CASCADE
                    )
                """)
                db.execSQL("CREATE INDEX IF NOT EXISTS index_set_logs_exerciseLogId ON set_logs(exerciseLogId)")
            }
        }
    }
}