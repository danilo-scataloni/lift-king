# AGENTS.md

## Objetivo do projeto

LiftKing e um app Android para gerenciamento de treinos de musculacao com a hierarquia:

`Periodization -> Workout -> WorkoutExercise -> TrainingSession -> ExerciseLog -> SetLog`

Stack principal:

- Kotlin 2.0.21
- Android app com Jetpack Compose + Material 3
- Room para persistencia local
- Koin para injecao de dependencia
- Navigation Compose com rotas serializaveis
- MVVM + separacao entre `domain`, `data` e `ui`

## Estrutura relevante

- `app/src/main/java/com/daniloscataloni/liftking/domain/models`: modelos de dominio puros
- `app/src/main/java/com/daniloscataloni/liftking/data/entities`: entidades do Room
- `app/src/main/java/com/daniloscataloni/liftking/data/daos`: DAOs
- `app/src/main/java/com/daniloscataloni/liftking/data/mappers`: conversao entre entity e domain
- `app/src/main/java/com/daniloscataloni/liftking/data/repositories`: interfaces e implementacoes de repositorio
- `app/src/main/java/com/daniloscataloni/liftking/ui/screens`: telas Compose
- `app/src/main/java/com/daniloscataloni/liftking/ui/viewmodels`: ViewModels com `StateFlow`
- `app/src/main/java/com/daniloscataloni/liftking/ui/components`: componentes reutilizaveis
- `app/src/main/java/com/daniloscataloni/liftking/navigation`: rotas e `NavHost`
- `app/src/main/java/com/daniloscataloni/liftking/di/Koin.kt`: composicao de DI
- `app/src/test`: testes unitarios
- `app/src/androidTest`: testes instrumentados

## Comandos uteis

Executar a partir da raiz do repositorio:

```bash
./gradlew assembleDebug
./gradlew test
./gradlew connectedTest
./gradlew ktlintCheck
./gradlew ktlintFormat
./gradlew detekt
```

## Regras de trabalho para agentes

- Preserve a separacao entre camadas. A UI nao deve operar com entidades Room diretamente.
- Ao mover dados entre `data` e `domain`, reutilize ou expanda os mappers em `data/mappers/EntityMappers.kt`.
- ViewModels usam `StateFlow` e `viewModelScope`; nao introduza `LiveData`.
- A injecao de dependencia e feita com Koin. Registre novos bindings em `app/src/main/java/com/daniloscataloni/liftking/di/Koin.kt`.
- Navegacao usa rotas serializaveis em `app/src/main/java/com/daniloscataloni/liftking/navigation/Routes.kt`.
- Repositorios seguem o padrao interface `I*Repository`. Algumas implementacoes ficam aninhadas dentro da propria interface, como `IWorkoutRepository.WorkoutRepository`.
- Prefira manter nomes e idioma consistentes com o codigo atual: classes e APIs em ingles, textos de UI em portugues quando fizer sentido ao produto.

## Room e migracoes

- O banco principal e `lift-king-database`.
- A versao atual do schema em codigo e `3`, definida em `app/src/main/java/com/daniloscataloni/liftking/data/LiftKingDatabase.kt`.
- Ja existem as migracoes `MIGRATION_1_2` e `MIGRATION_2_3`.
- Qualquer alteracao de schema exige:
  - atualizar `version`
  - criar a `Migration` correspondente
  - registrar a migracao no `Room.databaseBuilder(...)` em `Koin.kt`
- Nao use `fallbackToDestructiveMigration()`.

## Qualidade e validacao

- Rode `./gradlew test`, `./gradlew ktlintCheck` e `./gradlew detekt` antes de concluir mudancas relevantes.
- O projeto usa JUnit 5 para testes unitarios e MockK para mocking.
- `lint` esta com `warningsAsErrors = true`; trate warnings como falhas.

## Armadilhas especificas do projeto

- `app/build.gradle.kts` calcula `versionName` com `git describe --tags --abbrev=0`. Em ambiente sem tag Git, o build pode falhar.
- O `README.md` e o `CLAUDE.md` sao uteis como contexto, mas valide sempre no codigo quando houver divergencia.
- Ha detalhes de dominio recentes relacionados a `WeightUnit`; ao tocar em exercicios, logs ou UI de treino, confira impacto em mappers, banco e testes.

## Ao adicionar funcionalidade

1. Ajuste o modelo de dominio, se necessario.
2. Atualize entities, DAOs, repositorios e mappers de forma coerente.
3. Registre dependencias novas no Koin.
4. Atualize ViewModel e tela Compose.
5. Cubra a regra de negocio com teste unitario.
6. Se houver mudanca de persistencia, entregue migracao.

## Ao revisar ou depurar

- Comece por `README.md`, `CLAUDE.md`, `app/build.gradle.kts` e `app/src/main/java/com/daniloscataloni/liftking/di/Koin.kt`.
- Para fluxos de treino, a entrada principal costuma passar por `TrainingViewModel`.
- Para listas e navegacao principal, confira `PeriodizationScreen`, `WorkoutListScreen`, `TrainingScreen` e `LiftKingNavHost`.
