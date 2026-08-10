# Compose Counter Architecture Lab

这个仓库用于对比同一个 Jetpack Compose 计数器功能在两种 Android 架构下的实现方式：

- `CounterLab-MVVM`：向 MVVM 演进的版本
- `CounterLab-MVI`：向 MVI 演进的版本

它适合 Kotlin / Android 初学者用来观察：

- Compose 页面如何显示状态；
- ViewModel 如何管理状态；
- Repository 在简单项目中如何承担数据规则；
- MVVM 和 MVI 在事件传递、状态更新上的差异。

## 项目结构

```text
compose-counter-architecture-lab/
├── CounterLab-MVVM/
│   ├── app/
│   ├── docs/kotlin-guide-for-beginners.md
│   └── README.md
└── CounterLab-MVI/
    ├── app/
    ├── docs/kotlin-guide-for-beginners.md
    └── README.md
```

## 两个版本的核心区别

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

1. 先读 `CounterLab-MVVM/docs/kotlin-guide-for-beginners.md`
2. 再读 `CounterLab-MVI/docs/kotlin-guide-for-beginners.md`
3. 对比两个项目中的 `CounterScreen.kt`
4. 对比两个项目中的 `CounterViewModel.kt`
5. 最后阅读 MVI 版本中的 `CounterContract.kt` 和 `CounterReducer.kt`

## 构建方式

两个目录都是独立 Android 项目，可以分别进入目录构建：

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

