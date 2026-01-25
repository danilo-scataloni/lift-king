# LiftKing

Aplicativo minimalista Android para gerenciamento de treinos de musculação com periodização.

## Sobre

LiftKing permite criar ciclos de treino (periodizações), definir treinos com exercícios, e registrar séries com peso, repetições e RIR (Reps In Reserve) para autoregulação.

### Hierarquia

```
Periodização → Treinos → Exercícios → Séries
```

## Funcionalidades

- **Periodizações**: Criar e gerenciar ciclos de treino (ativo/arquivado)
- **Treinos**: Definir treinos dentro de cada periodização
- **Exercícios**: Biblioteca de exercícios com grupos musculares
- **Registro de Séries**: Peso (kg), repetições e RIR
- **Histórico**: Comparar desempenho atual com treinos anteriores

## Tech Stack

| Componente | Tecnologia |
|------------|------------|
| Linguagem | Kotlin 2.0 |
| UI | Jetpack Compose + Material3 |
| Banco de Dados | Room |
| DI | Koin |
| Navegação | Navigation Compose (type-safe) |
| Arquitetura | Clean Architecture + MVVM |

## Estrutura do Projeto

```
com/daniloscataloni/liftking/
├── domain/models/       # Entidades de domínio
├── data/
│   ├── entities/        # Entidades Room
│   ├── daos/            # Data Access Objects
│   ├── repositories/    # Repositórios
│   └── mappers/         # Conversores Entity ↔ Domain
├── ui/
│   ├── screens/         # Telas (Compose)
│   ├── viewmodels/      # ViewModels
│   ├── components/      # Componentes reutilizáveis
│   └── theme/           # Tema Material3
├── navigation/          # Rotas e NavHost
└── di/                  # Módulos Koin
```

## Requisitos

- Android SDK 25+ (Android 7.1)
- Java 11

## Build

```bash
./gradlew assembleDebug
```

## Testes

```bash
./gradlew test          # Unit tests
./gradlew connectedTest # Instrumentation tests
```

## Qualidade de Código

```bash
./gradlew ktlintCheck   # Kotlin linting
./gradlew detekt        # Static analysis
```

## Versionamento

Versão é calculada automaticamente a partir das git tags:

```bash
git tag -a v1.2.0 -m "descrição"
git push origin v1.2.0
```
