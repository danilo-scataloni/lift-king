# LiftKing — CLAUDE.md

## Project Overview

Android workout tracking app built with Clean Architecture + MVVM.
Business hierarchy: **Periodization → Workout → WorkoutExercise → TrainingSession → ExerciseLog → SetLog**

## Commands

```bash
# Build
./gradlew assembleDebug

# Tests
./gradlew test                # Unit tests (JUnit 5)
./gradlew connectedTest       # Instrumentation tests

# Code quality (run before committing)
./gradlew ktlintCheck         # Linting
./gradlew ktlintFormat        # Auto-fix lint issues
./gradlew detekt              # Static analysis

# Versioning — version code is auto-calculated from git tags
git tag -a v1.2.0 -m "Release v1.2.0"
```

## Architecture

Three-layer Clean Architecture:

```
domain/models/        # Pure Kotlin data classes — no Android deps
data/
  entities/           # Room @Entity classes (suffix: Entity)
  daos/               # Room @Dao interfaces (suffix: Dao)
  repositories/       # Repository implementations
  mappers/            # Entity ↔ Domain converters (always use mappers, never expose entities to UI)
ui/
  screens/            # Composable screens
  viewmodels/         # StateFlow-based ViewModels
  components/         # Reusable Compose components
navigation/           # Type-safe routes via @Serializable sealed interface
di/                   # Koin modules
```

## Key Conventions

- Repository interfaces are prefixed with `I` (e.g., `IWorkoutRepository`)
- Entities stay in the data layer — domain models are passed to the UI layer via mappers
- Navigation uses **Kotlinx Serialization** for type-safe routes (see `navigation/Route.kt`)
- DI is via **Koin** DSL (not Hilt) — all modules defined in `di/Koin.kt`
- ViewModels expose `StateFlow`, not `LiveData`
- Use `viewModelScope` + coroutines for async ops; repositories use `Flow` for reactive queries
- Database is `lift-king-database`, currently at **version 2** — always add a `Migration` when changing the schema

## Tech Stack

| Area | Library |
|------|---------|
| UI | Jetpack Compose + Material3 (dark theme) |
| State | StateFlow / Flow |
| DB | Room v2.8.4 (KSP) |
| DI | Koin v3.5.0 |
| Navigation | Navigation Compose (type-safe) |
| Serialization | Kotlinx Serialization JSON |
| Testing | JUnit 5 + MockK |
| Linting | Ktlint v12.1.2 + Detekt v1.23.7 |

## Database Notes

- Cascade deletes propagate down the hierarchy (Periodization → Workout → Session → SetLog)
- `WorkoutExercise` has `RESTRICT` on Exercise FK — cannot delete an exercise that is in use
- Only one `Periodization` can have `isActive = true` at a time — enforced at repository level
- Soft-delete via `isArchived` flag on `Periodization`

## Testing

Unit tests use **JUnit 5 (Jupiter)** — test tasks require `useJUnitPlatform()` (already configured).
Use **MockK** for mocking — not Mockito.
Repository interfaces are designed for easy mocking in tests.

## Detekt / Ktlint Config

- Detekt config: `config/detekt/detekt.yml`
- Detekt baseline: `config/detekt/baseline.xml`
- Ktlint runs in android mode
- Lint treats warnings as errors (`warningsAsErrors = true`)
