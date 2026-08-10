# 架构分析：MVI (Model-View-Intent)

本项目是一个**手写 MVI** 架构（非 MVVM），使用 Jetpack Compose + AndroidX ViewModel + Kotlin Coroutines/Flows 实现，无第三方 MVI 库依赖。

---

## 核心组件

| 组件 | 文件 | 职责 |
|------|------|------|
| **Intent** | `CounterContract.kt` | 密封接口，枚举所有用户操作（Increment / Decrement / Reset） |
| **State** | `CounterContract.kt` | 单一不可变数据类 `CounterState(count: Int)` |
| **Effect** | `CounterContract.kt` | 密封接口，一次性副作用（如 Snackbar 消息） |
| **Reducer** | `CounterReducer.kt` | 纯函数 `(State, Intent) -> State`，不含副作用 |
| **ViewModel** | `CounterViewModel.kt` | 生命周期感知容器，桥接 Reducer → StateFlow/Effect Channel |
| **Screen** | `CounterScreen.kt` | Compose UI，消费 state，分发 intent |
| **Repository** | `CounterRepository.kt` | 数据层，隔离计算逻辑 |

## 数据流

```
User Action
    ↓
dispatch(CounterIntent)     ← CounterScreen
    ↓
CounterViewModel.dispatch()
    ↓
CounterReducer.reduce()     ← 纯函数，返回新 State
    ↓
_state.update { ... }       ← StateFlow 更新
    ↓
collectAsStateWithLifecycle() ← UI 重组

同时：
    effectChannel.send()     ← 一次性 Effect（如 Reset 时弹 Snackbar）
    ↓
effects.collect { ... }     ← LaunchedEffect 消费
```

**严格单向：** UI 从不直接修改状态，只能通过 `dispatch(Intent)` 表达意图；状态变更完全由 Reducer 驱动。

## 与 MVVM 的关键区别

| 维度 | MVVM | MVI（本项目） |
|------|------|---------------|
| 用户操作 | 多个命名方法（`increment()`, `decrement()`） | 统一 `dispatch(Intent)` 入口 |
| State | 多个 StateFlow / LiveData | 单一不可变 State 类 |
| 状态变更 | ViewModel 内直接修改 MutableStateFlow | 纯 Reducer `(State, Intent) -> State` |
| 副作用 | 隐式混入 ViewModel 方法 | 显式 `Effect` 密封接口 + Channel |
| 可测试性 | 需 mock ViewModel/Android 依赖 | Reducer 纯函数，零依赖测试 |

## 测试策略

`CounterReducerTest.kt` 直接验证 Reducer 纯函数：

```kotlin
@Test
fun intentsProducePredictableImmutableStates() {
    assertEquals(CounterState(1), reducer.reduce(CounterState(), CounterIntent.Increment))
    assertEquals(CounterState(0), reducer.reduce(CounterState(1), CounterIntent.Decrement))
    assertEquals(CounterState(),   reducer.reduce(CounterState(7), CounterIntent.Reset))
}
```

## 依赖

仅标准 AndroidX 库：
- `androidx.lifecycle-runtime-ktx`
- `androidx.lifecycle-runtime-compose`
- `androidx.lifecycle-viewmodel-compose`
- Jetpack Compose (Material3, BOM)

无 Orbit MVI、Mavericks、MVIKotlin 等第三方 MVI 框架。
