# Compose Counter Architecture Lab

这个仓库记录了一个 Android Studio 单模块 Jetpack Compose 模板项目的三段学习路径：

- `CounterLab-Template`：原始模板项目，只显示 `Hello Android!`
- `CounterLab-MVVM`：向 MVVM 演进的版本
- `CounterLab-MVI`：向 MVI 演进的版本

它适合 Kotlin / Android 初学者用来观察：

- Android Studio Compose 模板项目的基本骨架；
- Compose 页面如何显示状态；
- ViewModel 如何管理状态；
- Repository 在简单项目中如何承担数据规则；
- MVVM 和 MVI 在事件传递、状态更新上的差异。

## 项目结构

```text
counter-lab/
├── CounterLab-Template/
│   ├── app/
│   ├── docs/kotlin-guide-for-beginners.md
│   └── README.md
├── CounterLab-MVVM/
│   ├── app/
│   ├── docs/kotlin-guide-for-beginners.md
│   └── README.md
└── CounterLab-MVI/
    ├── app/
    ├── docs/kotlin-guide-for-beginners.md
    └── README.md
```

## 三个目录之间的关系

```text
CounterLab-Template
        │
        ├── 演进为 CounterLab-MVVM
        │       UI 展示 UiState，用户操作调用 ViewModel 方法
        │
        └── 演进为 CounterLab-MVI
                UI 展示 State，用户操作发送 Intent
```

`CounterLab-Template` 是起点，用来学习 Kotlin + Compose 模板项目的最小结构；`CounterLab-MVVM` 和 `CounterLab-MVI` 则在同一个计数器功能上展示两种架构演进方式。

## MVVM 和 MVI 的核心区别

### MVVM

MVVM 版本里，UI 接收状态和多个回调：

```kotlin
CounterScreen(
    uiState = uiState,
    onIncrement = viewModel::increment,
    onDecrement = viewModel::decrement,
    onReset = viewModel::reset,
)
```

可以简单理解为：

```text
用户点击按钮
→ UI 调用 ViewModel 暴露的方法
→ ViewModel 更新 UiState
→ Compose 根据新状态刷新页面
```

### MVI

MVI 版本里，UI 通过统一入口发送 Intent：

```kotlin
CounterScreen(
    state = state,
    effects = viewModel.effects,
    dispatch = viewModel::dispatch,
)
```

可以简单理解为：

```text
用户点击按钮
→ UI 发送 Intent
→ ViewModel dispatch
→ Reducer 根据旧 State 算出新 State
→ Compose 根据新状态刷新页面
```

## 推荐阅读顺序

如果你是 Kotlin / Android 初学者，建议按这个顺序阅读：

1. 先读模板项目的 Kotlin 初学者导读；
2. 再读 MVVM 演进项目的 Kotlin 初学者导读；
3. 最后读 MVI 演进项目的 Kotlin 初学者导读；
4. 对比两个演进项目里的 `ui/CounterScreen.kt`
5. 对比两个演进项目里的 `ui/CounterViewModel.kt`
6. 最后阅读 MVI 版本里的 `ui/CounterContract.kt` 和 `ui/CounterReducer.kt`

三份导读在这里：

```text
counter-lab/
├── CounterLab-Template/
│   └── docs/kotlin-guide-for-beginners.md
├── CounterLab-MVVM/
│   └── docs/kotlin-guide-for-beginners.md
└── CounterLab-MVI/
    └── docs/kotlin-guide-for-beginners.md
```

相关文件可以按这个结构找：

```text
counter-lab/
├── CounterLab-MVVM/
│   └── app/src/main/java/com/counterlab/mvvm/ui/
│       ├── CounterScreen.kt
│       └── CounterViewModel.kt
└── CounterLab-MVI/
    └── app/src/main/java/com/counterlab/mvi/ui/
        ├── CounterScreen.kt
        ├── CounterViewModel.kt
        ├── CounterContract.kt
        └── CounterReducer.kt
```

## 构建方式

三个目录都是独立 Android 项目，可以分别进入目录构建：

```bash
cd CounterLab-Template
./gradlew testDebugUnitTest assembleDebug
```

```bash
cd CounterLab-MVVM
./gradlew testDebugUnitTest assembleDebug
```

```bash
cd CounterLab-MVI
./gradlew testDebugUnitTest assembleDebug
```

## 适合学习的知识点

- Kotlin 基础语法：`class`、`fun`、`val`、`data class`、函数类型、函数引用
- Jetpack Compose：`@Composable`、`State`、`Modifier`、Material3 组件
- Android Architecture：`ViewModel`、`StateFlow`
- MVVM：View / ViewModel / Repository 的分工
- MVI：State / Intent / Effect / Reducer 的单向数据流

## License

[Apache-2.0](LICENSE) © 2026 comicbase
