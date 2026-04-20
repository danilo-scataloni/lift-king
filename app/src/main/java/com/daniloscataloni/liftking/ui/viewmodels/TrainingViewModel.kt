package com.daniloscataloni.liftking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniloscataloni.liftking.data.repositories.IExerciseRepository
import com.daniloscataloni.liftking.data.repositories.ITrainingRepository
import com.daniloscataloni.liftking.data.repositories.IWorkoutRepository
import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.domain.models.RestTimer
import com.daniloscataloni.liftking.domain.models.SetLog
import com.daniloscataloni.liftking.domain.models.WeightUnit
import com.daniloscataloni.liftking.domain.models.WorkoutExercise
import com.daniloscataloni.liftking.resttimer.AppVisibilityTracker
import com.daniloscataloni.liftking.resttimer.IRestTimerManager
import com.daniloscataloni.liftking.resttimer.RestTimerScheduleMode
import com.daniloscataloni.liftking.resttimer.RestTimerStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

data class ActiveRestTimerUiState(
    val exerciseId: Int,
    val exerciseName: String,
    val durationSeconds: Int,
    val remainingSeconds: Int,
    val endAtEpochMillis: Long
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
    val newExerciseWeightUnit: WeightUnit = WeightUnit.KG,
    val editingSet: SetLog? = null,
    val editingSetWeightUnit: WeightUnit = WeightUnit.KG,
    val deleteExerciseError: Boolean = false,
    val showEditExerciseDialog: Boolean = false,
    val exerciseToEdit: Exercise? = null,
    val editExerciseName: String = "",
    val editExercisePrimaryMuscle: MuscleGroup = MuscleGroup.CHEST,
    val editExerciseSecondaryMuscle: MuscleGroup? = null,
    val editExerciseWeightUnit: WeightUnit = WeightUnit.KG,
    val activeRestTimer: ActiveRestTimerUiState? = null,
    val lastRestDurationSeconds: Int = RestTimerStorage.DEFAULT_REST_DURATION_SECONDS,
    val showExactAlarmPermissionPrompt: Boolean = false,
    val restCompletionSignal: Int = 0
)

class TrainingViewModel(
    private val workoutRepository: IWorkoutRepository,
    private val trainingRepository: ITrainingRepository,
    private val exerciseRepository: IExerciseRepository,
    private val restTimerManager: IRestTimerManager,
    private val appVisibilityTracker: AppVisibilityTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingUiState())
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    private var workoutId: Long = 0
    private var reorderJob: Job? = null
    private var restTimerJob: Job? = null

    fun loadWorkout(workoutId: Long) {
        this.workoutId = workoutId
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.value = currentState.copy(isLoading = true)

            val workout = workoutRepository.getWorkoutById(workoutId)

            var session = trainingRepository.getInProgressSession(workoutId)
            if (session == null) {
                trainingRepository.startSession(workoutId)
                session = trainingRepository.getInProgressSession(workoutId)
            }

            val workoutExercises = workoutRepository.getExercisesForWorkout(workoutId).first()
            val allExercises = exerciseRepository.getAllExercises().first()

            val currentExerciseLogs = session?.id?.let { trainingRepository.getExerciseLogs(it) } ?: emptyList()

            val exercisesWithSets = workoutExercises.map { workoutExercise ->
                val exercise = allExercises.find { it.id == workoutExercise.exerciseId }
                    ?: Exercise(
                        id = workoutExercise.exerciseId,
                        description = "",
                        primaryMuscleGroup = MuscleGroup.CHEST,
                        secondaryMuscleGroups = null
                    )

                val lastSets = trainingRepository.getLastSetsForExercise(workoutExercise.exerciseId, workoutId)
                val currentExerciseLog = currentExerciseLogs.find { it.exerciseId == workoutExercise.exerciseId }
                val currentSets = currentExerciseLog?.let {
                    trainingRepository.getSetsForExerciseLog(it.id)
                } ?: emptyList()

                ExerciseWithSets(
                    workoutExercise = workoutExercise,
                    exercise = exercise,
                    lastSets = lastSets,
                    currentSets = currentSets
                )
            }

            val exerciseIdsInWorkout = workoutExercises.map { it.exerciseId }.toSet()
            val availableExercises = allExercises.filter { it.id !in exerciseIdsInWorkout }
            val activeRestTimer = restTimerManager.getActiveTimer()
                ?.takeIf { it.workoutId == workoutId }
                ?.toUiState()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                workoutName = workout?.name ?: "",
                exercises = exercisesWithSets,
                sessionId = session?.id,
                availableExercises = availableExercises,
                showAddExerciseDialog = false,
                activeRestTimer = activeRestTimer,
                lastRestDurationSeconds = restTimerManager.getLastUsedDurationSeconds()
            )

            if (activeRestTimer != null) {
                startRestTimerCountdown(activeRestTimer)
            } else {
                restTimerJob?.cancel()
            }
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
            newExerciseSecondaryMuscle = null,
            newExerciseWeightUnit = WeightUnit.KG
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

    fun onNewExerciseWeightUnitChange(unit: WeightUnit) {
        _uiState.value = _uiState.value.copy(newExerciseWeightUnit = unit)
    }

    fun createExerciseAndAddToWorkout() {
        val name = _uiState.value.newExerciseName
        if (name.isBlank()) return

        viewModelScope.launch {
            val exercise = Exercise(
                id = 0,
                description = name,
                primaryMuscleGroup = _uiState.value.newExercisePrimaryMuscle,
                secondaryMuscleGroups = _uiState.value.newExerciseSecondaryMuscle,
                weightUnit = _uiState.value.newExerciseWeightUnit
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
            val exerciseLog = exerciseLogs.find { it.exerciseId == exerciseId }

            val exerciseLogId = exerciseLog?.id ?: trainingRepository.createExerciseLog(sessionId, exerciseId)

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

    fun startRestTimer(exerciseId: Int, exerciseName: String, durationSeconds: Int) {
        if (durationSeconds <= 0 || workoutId <= 0L) return

        val now = System.currentTimeMillis()
        val restTimer = RestTimer(
            workoutId = workoutId,
            exerciseId = exerciseId,
            exerciseName = exerciseName,
            workoutName = _uiState.value.workoutName,
            durationSeconds = durationSeconds,
            startAtEpochMillis = now,
            endAtEpochMillis = now + durationSeconds * 1000L
        )

        val result = restTimerManager.scheduleRestTimer(restTimer)
        val activeRestTimer = result.timer.toUiState()

        _uiState.value = _uiState.value.copy(
            activeRestTimer = activeRestTimer,
            lastRestDurationSeconds = durationSeconds,
            showExactAlarmPermissionPrompt = result.scheduleMode == RestTimerScheduleMode.INEXACT
        )

        startRestTimerCountdown(activeRestTimer)
    }

    fun cancelRestTimer() {
        restTimerJob?.cancel()
        restTimerManager.cancelActiveTimer()
        _uiState.value = _uiState.value.copy(
            activeRestTimer = null,
            showExactAlarmPermissionPrompt = false
        )
    }

    fun extendRestTimer(additionalSeconds: Int = 30) {
        val activeRestTimer = _uiState.value.activeRestTimer ?: return
        startRestTimer(
            exerciseId = activeRestTimer.exerciseId,
            exerciseName = activeRestTimer.exerciseName,
            durationSeconds = activeRestTimer.remainingSeconds + additionalSeconds
        )
    }

    fun dismissExactAlarmPermissionPrompt() {
        _uiState.value = _uiState.value.copy(showExactAlarmPermissionPrompt = false)
    }

    fun consumeRestCompletionSignal() {
        _uiState.value = _uiState.value.copy(restCompletionSignal = 0)
    }

    fun completeSession() {
        viewModelScope.launch {
            val sessionId = _uiState.value.sessionId ?: return@launch
            cancelRestTimer()
            trainingRepository.completeSession(sessionId)
            _uiState.value = _uiState.value.copy(sessionId = null)
        }
    }

    fun startEditingSet(set: SetLog) {
        val unit = _uiState.value.exercises
            .find { exerciseWithSets -> exerciseWithSets.currentSets.any { it.id == set.id } }
            ?.exercise?.weightUnit ?: WeightUnit.KG
        _uiState.value = _uiState.value.copy(editingSet = set, editingSetWeightUnit = unit)
    }

    fun cancelEditingSet() {
        _uiState.value = _uiState.value.copy(editingSet = null, editingSetWeightUnit = WeightUnit.KG)
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
            _uiState.value = _uiState.value.copy(editingSet = null, editingSetWeightUnit = WeightUnit.KG)
            loadWorkout(workoutId)
        }
    }

    fun deleteSet() {
        val setToDelete = _uiState.value.editingSet ?: return

        viewModelScope.launch {
            trainingRepository.deleteSet(setToDelete)
            _uiState.value = _uiState.value.copy(editingSet = null, editingSetWeightUnit = WeightUnit.KG)
            loadWorkout(workoutId)
        }
    }

    fun onShowEditExerciseDialog(exercise: Exercise) {
        _uiState.value = _uiState.value.copy(
            showEditExerciseDialog = true,
            exerciseToEdit = exercise,
            editExerciseName = exercise.description,
            editExercisePrimaryMuscle = exercise.primaryMuscleGroup,
            editExerciseSecondaryMuscle = exercise.secondaryMuscleGroups,
            editExerciseWeightUnit = exercise.weightUnit
        )
    }

    fun onDismissEditExerciseDialog() {
        _uiState.value = _uiState.value.copy(
            showEditExerciseDialog = false,
            exerciseToEdit = null
        )
    }

    fun onEditExerciseNameChange(name: String) {
        _uiState.value = _uiState.value.copy(editExerciseName = name)
    }

    fun onEditExercisePrimaryMuscleChange(muscle: MuscleGroup) {
        if (muscle == _uiState.value.editExerciseSecondaryMuscle) return
        _uiState.value = _uiState.value.copy(editExercisePrimaryMuscle = muscle)
    }

    fun onEditExerciseSecondaryMuscleChange(muscle: MuscleGroup?) {
        if (muscle == _uiState.value.editExercisePrimaryMuscle) return
        _uiState.value = _uiState.value.copy(editExerciseSecondaryMuscle = muscle)
    }

    fun onEditExerciseWeightUnitChange(unit: WeightUnit) {
        _uiState.value = _uiState.value.copy(editExerciseWeightUnit = unit)
    }

    fun confirmEditExercise() {
        val exercise = _uiState.value.exerciseToEdit ?: return
        val name = _uiState.value.editExerciseName.trim()
        if (name.isBlank()) return

        viewModelScope.launch {
            exerciseRepository.updateExercise(
                exercise.copy(
                    description = name,
                    primaryMuscleGroup = _uiState.value.editExercisePrimaryMuscle,
                    secondaryMuscleGroups = _uiState.value.editExerciseSecondaryMuscle,
                    weightUnit = _uiState.value.editExerciseWeightUnit
                )
            )
            onDismissEditExerciseDialog()
            loadWorkout(workoutId)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            try {
                exerciseRepository.deleteExercise(exercise)
                val exerciseIdsInWorkout = _uiState.value.exercises.map { it.exercise.id }.toSet()
                val updated = exerciseRepository.getAllExercises().first()
                    .filter { it.id !in exerciseIdsInWorkout }
                _uiState.value = _uiState.value.copy(availableExercises = updated)
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(deleteExerciseError = true)
            }
        }
    }

    fun clearDeleteExerciseError() {
        _uiState.value = _uiState.value.copy(deleteExerciseError = false)
    }

    fun removeExerciseFromWorkout(exerciseId: Int) {
        reorderJob?.cancel()
        viewModelScope.launch {
            workoutRepository.removeExerciseFromWorkout(workoutId, exerciseId)
            loadWorkout(workoutId)
        }
    }

    fun moveExercise(fromIndex: Int, toIndex: Int) {
        val size = _uiState.value.exercises.size
        if (fromIndex !in 0 until size || toIndex !in 0 until size) return

        val currentList = _uiState.value.exercises.toMutableList()
        val item = currentList.removeAt(fromIndex)
        currentList.add(toIndex, item)

        val reindexed = currentList.mapIndexed { index, exerciseWithSets ->
            exerciseWithSets.copy(
                workoutExercise = exerciseWithSets.workoutExercise.copy(order = index)
            )
        }

        _uiState.value = _uiState.value.copy(exercises = reindexed)

        reorderJob?.cancel()
        reorderJob = viewModelScope.launch {
            delay(500)
            workoutRepository.reorderExercises(reindexed.map { it.workoutExercise })
        }
    }

    internal fun seedExercisesForTest(exercises: List<ExerciseWithSets>) {
        _uiState.value = _uiState.value.copy(exercises = exercises, isLoading = false)
    }

    private fun startRestTimerCountdown(activeRestTimer: ActiveRestTimerUiState) {
        restTimerJob?.cancel()
        restTimerJob = viewModelScope.launch {
            while (true) {
                val storedTimer = restTimerManager.getActiveTimer()
                if (storedTimer == null || !storedTimer.matches(activeRestTimer)) {
                    clearLocalRestTimerState()
                    break
                }

                val remainingSeconds = calculateRemainingSeconds(activeRestTimer.endAtEpochMillis)
                _uiState.value = _uiState.value.copy(
                    activeRestTimer = activeRestTimer.copy(remainingSeconds = remainingSeconds)
                )

                if (remainingSeconds <= 0 && appVisibilityTracker.isAppInForeground) {
                    finishRestTimerInApp()
                    break
                }

                delay(1000)
            }
        }
    }

    private fun finishRestTimerInApp() {
        restTimerJob?.cancel()
        restTimerManager.cancelActiveTimer()
        _uiState.value = _uiState.value.copy(
            activeRestTimer = null,
            showExactAlarmPermissionPrompt = false,
            restCompletionSignal = _uiState.value.restCompletionSignal + 1
        )
    }

    private fun clearLocalRestTimerState() {
        restTimerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            activeRestTimer = null,
            showExactAlarmPermissionPrompt = false
        )
    }

    private fun calculateRemainingSeconds(endAtEpochMillis: Long): Int {
        val remainingMillis = (endAtEpochMillis - System.currentTimeMillis()).coerceAtLeast(0L)
        return ((remainingMillis + 999L) / 1000L).toInt()
    }

    private fun RestTimer.toUiState(): ActiveRestTimerUiState {
        return ActiveRestTimerUiState(
            exerciseId = exerciseId,
            exerciseName = exerciseName,
            durationSeconds = durationSeconds,
            remainingSeconds = calculateRemainingSeconds(endAtEpochMillis),
            endAtEpochMillis = endAtEpochMillis
        )
    }

    private fun RestTimer.matches(activeRestTimer: ActiveRestTimerUiState): Boolean {
        return workoutId == this@TrainingViewModel.workoutId &&
            exerciseId == activeRestTimer.exerciseId &&
            endAtEpochMillis == activeRestTimer.endAtEpochMillis
    }

    override fun onCleared() {
        reorderJob?.cancel()
        restTimerJob?.cancel()
        super.onCleared()
    }
}
