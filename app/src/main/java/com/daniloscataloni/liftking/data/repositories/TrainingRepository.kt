package com.daniloscataloni.liftking.data.repositories

import com.daniloscataloni.liftking.data.daos.ExerciseLogDao
import com.daniloscataloni.liftking.data.daos.SetLogDao
import com.daniloscataloni.liftking.data.daos.TrainingSessionDao
import com.daniloscataloni.liftking.data.entities.ExerciseLogEntity
import com.daniloscataloni.liftking.data.entities.TrainingSessionEntity
import com.daniloscataloni.liftking.data.mappers.toDomain
import com.daniloscataloni.liftking.data.mappers.toEntity
import com.daniloscataloni.liftking.domain.models.ExerciseLog
import com.daniloscataloni.liftking.domain.models.SetLog
import com.daniloscataloni.liftking.domain.models.TrainingSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ITrainingRepository {
    // Session operations
    fun getSessionsByWorkout(workoutId: Long): Flow<List<TrainingSession>>
    suspend fun getInProgressSession(workoutId: Long): TrainingSession?
    suspend fun getLastCompletedSession(workoutId: Long): TrainingSession?
    suspend fun startSession(workoutId: Long): Long
    suspend fun completeSession(sessionId: Long)
    suspend fun updateSessionNotes(sessionId: Long, notes: String)
    suspend fun deleteSession(session: TrainingSession)
    
    // Exercise log operations
    suspend fun getExerciseLogs(sessionId: Long): List<ExerciseLog>
    suspend fun createExerciseLog(sessionId: Long, exerciseId: Int): Long
    suspend fun updateExerciseLogNotes(logId: Long, notes: String)
    
    // Set log operations
    suspend fun getLastSetsForExercise(exerciseId: Int, workoutId: Long): List<SetLog>
    suspend fun getSetsForExerciseLog(exerciseLogId: Long): List<SetLog>
    suspend fun logSet(setLog: SetLog): Long
    suspend fun updateSet(setLog: SetLog)
    suspend fun deleteSet(setLog: SetLog)
}

class TrainingRepository(
    private val sessionDao: TrainingSessionDao,
    private val exerciseLogDao: ExerciseLogDao,
    private val setLogDao: SetLogDao
) : ITrainingRepository {

    // --- Session operations ---
    
    override fun getSessionsByWorkout(workoutId: Long): Flow<List<TrainingSession>> {
        return sessionDao.getSessionsByWorkout(workoutId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getInProgressSession(workoutId: Long): TrainingSession? {
        return sessionDao.getInProgressSession(workoutId)?.toDomain()
    }
    
    override suspend fun getLastCompletedSession(workoutId: Long): TrainingSession? {
        return sessionDao.getLastSessionForWorkout(workoutId)?.toDomain()
    }
    
    override suspend fun startSession(workoutId: Long): Long {
        val session = TrainingSessionEntity(
            workoutId = workoutId,
            date = System.currentTimeMillis(),
            isCompleted = false
        )
        return sessionDao.insert(session)
    }
    
    override suspend fun completeSession(sessionId: Long) {
        sessionDao.getById(sessionId)?.let { session ->
            sessionDao.update(session.copy(isCompleted = true))
        }
    }
    
    override suspend fun updateSessionNotes(sessionId: Long, notes: String) {
        sessionDao.getById(sessionId)?.let { session ->
            sessionDao.update(session.copy(notes = notes))
        }
    }
    
    override suspend fun deleteSession(session: TrainingSession) {
        sessionDao.delete(session.toEntity())
    }

    // --- Exercise log operations ---
    
    override suspend fun getExerciseLogs(sessionId: Long): List<ExerciseLog> {
        return exerciseLogDao.getBySessionOnce(sessionId).map { it.toDomain() }
    }
    
    override suspend fun createExerciseLog(sessionId: Long, exerciseId: Int): Long {
        val log = ExerciseLogEntity(
            sessionId = sessionId,
            exerciseId = exerciseId
        )
        return exerciseLogDao.insert(log)
    }
    
    override suspend fun updateExerciseLogNotes(logId: Long, notes: String) {
        exerciseLogDao.getById(logId)?.let { log ->
            exerciseLogDao.update(log.copy(notes = notes))
        }
    }

    // --- Set log operations ---
    
    /**
     * Busca todas as séries do último treino completado
     * para um exercício específico dentro de um workout.
     */
    override suspend fun getLastSetsForExercise(exerciseId: Int, workoutId: Long): List<SetLog> {
        return setLogDao.getLastSetsForExerciseInWorkout(exerciseId, workoutId)
            .map { it.toDomain() }
    }
    
    override suspend fun getSetsForExerciseLog(exerciseLogId: Long): List<SetLog> {
        return setLogDao.getByExerciseLogOnce(exerciseLogId).map { it.toDomain() }
    }
    
    override suspend fun logSet(setLog: SetLog): Long {
        return setLogDao.insert(setLog.toEntity())
    }
    
    override suspend fun updateSet(setLog: SetLog) {
        setLogDao.update(setLog.toEntity())
    }
    
    override suspend fun deleteSet(setLog: SetLog) {
        setLogDao.delete(setLog.toEntity())
    }
}
