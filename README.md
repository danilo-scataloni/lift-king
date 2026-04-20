# LiftKing

Aplicativo minimalista Android para gerenciamento de treinos de musculação com periodização.

## Sobre

LiftKing permite criar ciclos de treino (periodizações), definir treinos com exercícios, e registrar séries com peso, repetições e RIR (Reps In Reserve) para autoregulação.

A navegação principal usa uma barra inferior com duas abas:

- **Treinos**: mantém o fluxo `Periodizações -> Treinos -> Training`
- **Exercícios**: biblioteca global de exercícios com edição e exclusão

### Hierarquia

```
Periodização → Treinos → Exercícios → Séries
```

## Funcionalidades

- **Periodizações**: Criar e gerenciar ciclos de treino (ativo/arquivado)
- **Treinos**: Definir treinos dentro de cada periodização
- **Exercícios**: Biblioteca global com cards, grupos musculares e unidade de peso
- **Registro de Séries**: Peso (kg), repetições e RIR
- **Timer de Descanso**: Salvar série e iniciar descanso com presets, tempo customizado e atalho de `+30s`
- **Alertas em Background**: Notificação de descanso finalizado mesmo fora do app, com abertura direta do treino
- **Histórico**: Comparar desempenho atual com treinos anteriores
- **Idiomas**: Interface localizada em português e inglês, seguindo o idioma do sistema
- **Bottom Navigation**: Alternância rápida entre treinos e biblioteca de exercícios

## Tech Stack

| Componente | Tecnologia |
|------------|------------|
| Linguagem | Kotlin 2.0 |
| UI | Jetpack Compose + Material3 |
| Banco de Dados | Room |
| DI | Koin |
| Navegação | Navigation Compose (type-safe) + bottom navigation |
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

## Descanso e Notificações

- O fluxo de treino suporta descanso entre séries direto na `TrainingScreen`.
- Com o app aberto, o cronômetro aparece em uma faixa full-width no topo da tela de treino.
- Com o app em background, o app mantém notificação de descanso em andamento e mostra alerta quando o tempo termina.
- Em Android 13+, o app pode pedir permissão de notificações (`POST_NOTIFICATIONS`).
- Em Android 12+, o app pode sugerir habilitar alarmes exatos (`SCHEDULE_EXACT_ALARM`) para melhorar a precisão do alerta.

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
