package com.daniloscataloni.liftking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.domain.models.SetLog
import com.daniloscataloni.liftking.domain.models.WorkoutExercise
import com.daniloscataloni.liftking.data.repositories.IExerciseRepository
import com.daniloscataloni.liftking.data.repositories.ITrainingRepository
import com.daniloscataloni.liftking.data.repositories.IWorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ExerciseWithSets(
    val workoutExercise: WorkoutExercise,
    val exercise: Exercise,
    val lastSets: List<SetLog>,
    val currentSets: List<SetLog>
)

data class TrainingUiState(
    val isLoading: Boolean = true,
    val workoutName: String = "",
    val exercises: List<ExerciseWithSets> = emptyList(),
    val sessionId: Long? = null,
    val availableExercises: List<Exercise> = emptyList(),
    val showAddExerciseDialog: Boolean = false,
    val showCreateExerciseDialog: Boolean = false,
    val newExerciseName: String = "",
    val newExercisePrimaryMuscle: MuscleGroup = MuscleGroup.CHEST,
    val newExerciseSecondaryMuscle: MuscleGroup? = null,
    val editingSet: SetLog? = null
)

class TrainingViewModel(
    private val workoutRepository: IWorkoutRepository,
    private val trainingRepository: ITrainingRepository,
    private val exerciseRepository: IExerciseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingUiState())
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    private var workoutId: Long = 0

    fun loadWorkout(workoutId: Long) {
        this.workoutId = workoutId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val workout = workoutRepository.getWorkoutById(workoutId)

            var session = trainingRepository.getInProgressSession(workoutId)
            if (session == null) {
                val sessionId = trainingRepository.startSession(workoutId)
                session = trainingRepository.getInProgressSession(workoutId)
            }

            val workoutExercises = workoutRepository.getExercisesForWorkout(workoutId).first()
            val allExercises = exerciseRepository.getAllExercises().first()

            val currentExerciseLogs = session?.id?.let { trainingRepository.getExerciseLogs(it) } ?: emptyList()

            val exercisesWithSets = workoutExercises.map { we ->
                val exercise = allExercises.find { it.id == we.exerciseId }
                    ?: Exercise(id = we.exerciseId, description = "Exercício desconhecido",
                        primaryMuscleGroup = MuscleGroup.CHEST,
                        secondaryMuscleGroups = null)

                val lastSets = trainingRepository.getLastSetsForExercise(we.exerciseId, workoutId)

                val currentExerciseLog = currentExerciseLogs.find { it.exerciseId == we.exerciseId }
                val currentSets = currentExerciseLog?.let {
                    trainingRepository.getSetsForExerciseLog(it.id)
                } ?: emptyList()

                ExerciseWithSets(
                    workoutExercise = we,
                    exercise = exercise,
                    lastSets = lastSets,
                    currentSets = currentSets
                )
            }

            val exerciseIdsInWorkout = workoutExercises.map { it.exerciseId }.toSet()
            val available = allExercises.filter { it.id !in exerciseIdsInWorkout }

            _uiState.value = TrainingUiState(
                isLoading = false,
                workoutName = workout?.name ?: "",
                exercises = exercisesWithSets,
                sessionId = session?.id,
                availableExercises = available,
                showAddExerciseDialog = false
            )
        }
    }

    fun showAddExerciseDialog() {
        _uiState.value = _uiState.value.copy(showAddExerciseDialog = true)
    }

    fun hideAddExerciseDialog() {
        _uiState.value = _uiState.value.copy(showAddExerciseDialog = false)
    }

    fun showCreateExerciseDialog() {
        _uiState.value = _uiState.value.copy(
            showCreateExerciseDialog = true,
            showAddExerciseDialog = false,
            newExerciseName = "",
            newExercisePrimaryMuscle = MuscleGroup.CHEST,
            newExerciseSecondaryMuscle = null
        )
    }

    fun hideCreateExerciseDialog() {
        _uiState.value = _uiState.value.copy(showCreateExerciseDialog = false)
    }

    fun onNewExerciseNameChange(name: String) {
        _uiState.value = _uiState.value.copy(newExerciseName = name)
    }

    fun onNewExercisePrimaryMuscleChange(muscle: MuscleGroup) {
        if (muscle == _uiState.value.newExerciseSecondaryMuscle) return
        _uiState.value = _uiState.value.copy(newExercisePrimaryMuscle = muscle)
    }

    fun onNewExerciseSecondaryMuscleChange(muscle: MuscleGroup?) {
        if (muscle == _uiState.value.newExercisePrimaryMuscle) return
        _uiState.value = _uiState.value.copy(newExerciseSecondaryMuscle = muscle)
    }

    fun createExerciseAndAddToWorkout() {
        val name = _uiState.value.newExerciseName
        if (name.isBlank()) return

        viewModelScope.launch {
            val exercise = Exercise(
                id = 0,
                description = name,
                primaryMuscleGroup = _uiState.value.newExercisePrimaryMuscle,
                secondaryMuscleGroups = _uiState.value.newExerciseSecondaryMuscle
            )
            val newId = exerciseRepository.insertExercise(exercise)

            val currentCount = _uiState.value.exercises.size
            workoutRepository.addExerciseToWorkout(
                WorkoutExercise(
                    workoutId = workoutId,
                    exerciseId = newId.toInt(),
                    order = currentCount
                )
            )

            hideCreateExerciseDialog()
            loadWorkout(workoutId)
        }
    }

    fun addExerciseToWorkout(exercise: Exercise) {
        viewModelScope.launch {
            val currentCount = _uiState.value.exercises.size
            workoutRepository.addExerciseToWorkout(
                WorkoutExercise(
                    workoutId = workoutId,
                    exerciseId = exercise.id,
                    order = currentCount
                )
            )
            hideAddExerciseDialog()
            loadWorkout(workoutId)
        }
    }

    fun logSet(exerciseId: Int, weight: Float, reps: Int, rir: Int?) {
        viewModelScope.launch {
            val sessionId = _uiState.value.sessionId ?: return@launch

            val exerciseLogs = trainingRepository.getExerciseLogs(sessionId)
            var exerciseLog = exerciseLogs.find { it.exerciseId == exerciseId }

            val exerciseLogId = if (exerciseLog == null) {
                trainingRepository.createExerciseLog(sessionId, exerciseId)
            } else {
                exerciseLog.id
            }

            val currentSets = trainingRepository.getSetsForExerciseLog(exerciseLogId)
            val setNumber = currentSets.size + 1

            trainingRepository.logSet(
                SetLog(
                    exerciseLogId = exerciseLogId,
                    setNumber = setNumber,
                    weight = weight,
                    reps = reps,
                    rir = rir
                )
            )

            loadWorkout(workoutId)
        }
    }

    fun completeSession() {
        viewModelScope.launch {
            _uiState.value.sessionId?.let { sessionId ->
                trainingRepository.completeSession(sessionId)
            }
        }
    }

    fun startEditingSet(set: SetLog) {
        _uiState.value = _uiState.value.copy(editingSet = set)
    }

    fun cancelEditingSet() {
        _uiState.value = _uiState.value.copy(editingSet = null)
    }

    fun updateSet(weight: Float, reps: Int, rir: Int?) {
        val setToUpdate = _uiState.value.editingSet ?: return

        viewModelScope.launch {
            trainingRepository.updateSet(
                setToUpdate.copy(
                    weight = weight,
                    reps = reps,
                    rir = rir
                )
            )
            _uiState.value = _uiState.value.copy(editingSet = null)
            loadWorkout(workoutId)
        }
    }

    fun deleteSet() {
        val setToDelete = _uiState.value.editingSet ?: return

        viewModelScope.launch {
            trainingRepository.deleteSet(setToDelete)
            _uiState.value = _uiState.value.copy(editingSet = null)
            loadWorkout(workoutId)
        }
    }
}
