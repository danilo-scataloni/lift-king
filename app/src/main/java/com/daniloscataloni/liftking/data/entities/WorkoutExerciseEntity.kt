package com.daniloscataloni.liftking.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabela de junção entre Workout e Exercise.
 *
 * Por que não usar @Relation direta?
 * - Precisamos guardar dados extras: ordem e séries alvo
 * - Um exercício pode estar em VÁRIOS treinos (supino pode estar no Treino A e B)
 * - Permite configuração diferente por treino (3 séries no A, 5 séries no B)
 *
 * Relacionamentos:
 * - Workout (1) → (N) WorkoutExercise
 * - Exercise (1) → (N) WorkoutExercise
 */
@Entity(
    tableName = "workout_exercises",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            // RESTRICT: não permite deletar exercício se estiver em algum treino
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["workoutId"]),
        Index(value = ["exerciseId"])
    ]
)
data class WorkoutExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val workoutId: Long,
    val exerciseId: Int, // Int porque ExerciseEntity usa Int

    /**
     * Ordem do exercício dentro do treino.
     * Agachamento pode ser 0 (primeiro), Supino pode ser 1 (segundo), etc.
     */
    val order: Int = 0,

    /**
     * Número de séries alvo para este exercício neste treino.
     * Nullable porque nem sempre o usuário define metas.
     */
    val targetSets: Int? = null
)
