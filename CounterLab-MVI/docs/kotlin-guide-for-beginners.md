# MyDemo1-MVI Kotlin 初学者导读

这份导读面向还没有系统接触过 Kotlin 的同学。MVI 比 MVVM 多几个新词，比如 `Intent`、`State`、`Effect`、`Reducer`，刚开始看会有点像突然进了控制室：按钮很多，但其实每个按钮职责都很清楚。

这份文档会用通俗语言说明：

- MVI 版本的 App 是怎么工作的；
- 各个 Kotlin 文件分别负责什么；
- `sealed interface`、`data object`、`data class` 这些语法为什么会出现；
- 用户点击按钮后，状态是怎么一步步变化的。

## 1. 先用一句话看懂这个项目

这个 MVI 版本的核心可以这样理解：

> 用户操作先变成 `Intent`，ViewModel 把 Intent 交给 Reducer，Reducer 根据旧 State 算出新 State，UI 再根据新 State 刷新页面。

和 MVVM 相比，MVI 更强调“单向数据流”。也就是数据和事件尽量按一个方向走，不到处乱飞。

## 2. MVI 在本项目里分别是谁？

关键文件如下：

```text
app/src/main/java/com/example/mydemo1mvi/MainActivity.kt
app/src/main/java/com/example/mydemo1mvi/ui/CounterScreen.kt
app/src/main/java/com/example/mydemo1mvi/ui/CounterContract.kt
app/src/main/java/com/example/mydemo1mvi/ui/CounterReducer.kt
app/src/main/java/com/example/mydemo1mvi/ui/CounterViewModel.kt
app/src/main/java/com/example/mydemo1mvi/data/CounterRepository.kt
```

可以先按这个关系理解：

```text
View
└── CounterScreen.kt
    显示页面，把用户点击变成 Intent 发出去

Contract
└── CounterContract.kt
    定义 State、Intent、Effect，像一份“页面协议”

Reducer
└── CounterReducer.kt
    根据旧 State + Intent 算出新 State

ViewModel
└── CounterViewModel.kt
    接收 Intent，更新 State，发送 Effect

Repository
└── CounterRepository.kt
    提供计数计算规则

Activity
└── MainActivity.kt
    把 ViewModel 和 Compose 页面接起来
```

如果你暂时记不住这么多词，先记这条线：

```text
Intent 进来 → Reducer 算 State → UI 显示 State
```

## 3. `CounterContract`：先把页面协议写清楚

MVI 版本里最有代表性的文件是 `CounterContract.kt`：

```kotlin
data class CounterState(val count: Int = 0)

sealed interface CounterIntent {
    data object Increment : CounterIntent
    data object Decrement : CounterIntent
    data object Reset : CounterIntent
}

sealed interface CounterEffect {
    data class ShowMessage(val message: String) : CounterEffect
}
```

这份文件像是在告诉大家：

- 页面状态是什么；
- 用户可能做哪些操作；
- 页面可能产生哪些一次性效果。

### `CounterState`

```kotlin
data class CounterState(val count: Int = 0)
```

这是页面状态。当前只有一个 `count`。

MVI 很喜欢把整个页面状态集中放进一个 State 对象里。因为这样 UI 只要看一个 State，就知道自己该显示什么。

### `CounterIntent`

```kotlin
sealed interface CounterIntent {
    data object Increment : CounterIntent
    data object Decrement : CounterIntent
    data object Reset : CounterIntent
}
```

`Intent` 在这里不是 Android 的 `Intent`，而是 MVI 里的“用户意图”。

比如：

- 用户点了 `+1`，就是 `CounterIntent.Increment`；
- 用户点了 `-1`，就是 `CounterIntent.Decrement`；
- 用户点了 `Reset`，就是 `CounterIntent.Reset`。

为什么不用字符串 `"increment"`？因为字符串容易写错，而且编译器帮不上忙。用 Kotlin 类型表示意图，安全很多。

### `sealed interface` 是什么？

```kotlin
sealed interface CounterIntent
```

`sealed` 可以理解成“封闭的”。它表示这个接口有哪些实现，基本都在当前编译范围内明确列出来。

这样写的好处是：当你用 `when` 判断 `CounterIntent` 时，编译器知道一共有哪几种情况。

### `data object` 是什么？

```kotlin
data object Increment : CounterIntent
```

`Increment` 这种意图不需要携带额外数据，只需要表示“加一”这个动作，所以用 `object` 很合适。

`data object` 可以理解成：一个单例对象，而且适合拿来做数据比较和打印。

如果以后有一个意图需要带参数，比如“设置为某个数字”，可能会写成：

```kotlin
data class SetCount(val value: Int) : CounterIntent
```

这就是 Kotlin 类型系统好用的地方：不同事件可以自然表达不同数据。

### `CounterEffect`

```kotlin
sealed interface CounterEffect {
    data class ShowMessage(val message: String) : CounterEffect
}
```

`Effect` 表示一次性事件。比如弹 Snackbar、跳转页面、弹 Toast。

为什么不把 Snackbar 文案直接放进 `State`？因为 Snackbar 通常是“一次性动作”，不是页面长期状态。如果放进 State，可能会因为页面重组而重复弹。

所以 MVI 常把长期显示的数据叫 `State`，把一次性动作叫 `Effect`。

## 4. `CounterReducer`：像一个状态计算器

看 `CounterReducer.kt`：

```kotlin
class CounterReducer(private val repository: CounterRepository) {
    fun reduce(state: CounterState, intent: CounterIntent): CounterState = when (intent) {
        CounterIntent.Increment -> state.copy(
            count = repository.applyDelta(state.count, 1),
        )
        CounterIntent.Decrement -> state.copy(
            count = repository.applyDelta(state.count, -1),
        )
        CounterIntent.Reset -> CounterState()
    }
}
```

Reducer 的职责很纯粹：

> 给我一个旧 State 和一个 Intent，我返回一个新 State。

它不负责显示 UI，也不负责按钮点击。它像一个计算器。

### `when (intent)` 怎么读？

```kotlin
when (intent) {
    CounterIntent.Increment -> ...
    CounterIntent.Decrement -> ...
    CounterIntent.Reset -> ...
}
```

可以理解成 Kotlin 版的多分支判断：

- 如果是加一意图，就返回加一后的状态；
- 如果是减一意图，就返回减一后的状态；
- 如果是重置意图，就返回默认状态。

因为 `CounterIntent` 是 `sealed interface`，编译器知道目前只有这三种情况，所以这里不需要写 `else`。

### 为什么用 `copy`？

```kotlin
state.copy(count = repository.applyDelta(state.count, 1))
```

`CounterState` 是 `data class`，所以 Kotlin 自动提供 `copy`。

这行的意思是：

> 基于旧状态复制一个新状态，只把 count 改成新的值。

MVI 很强调状态不可变。不要在原对象上直接改来改去，而是每次产生一个新的 State。

## 5. `CounterViewModel`：接收 Intent，管理 State 和 Effect

看核心代码：

```kotlin
class CounterViewModel : ViewModel() {
    private val reducer = CounterReducer(InMemoryCounterRepository())
    private val _state = MutableStateFlow(CounterState())
    val state: StateFlow<CounterState> = _state.asStateFlow()

    private val effectChannel = Channel<CounterEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    fun dispatch(intent: CounterIntent) {
        _state.update { reducer.reduce(it, intent) }
        if (intent == CounterIntent.Reset) {
            viewModelScope.launch {
                effectChannel.send(CounterEffect.ShowMessage("Counter reset"))
            }
        }
    }
}
```

这里有三个重点。

### 第一，State 仍然用 `StateFlow`

```kotlin
private val _state = MutableStateFlow(CounterState())
val state: StateFlow<CounterState> = _state.asStateFlow()
```

这和 MVVM 版本很像：

- `_state` 是内部可修改状态；
- `state` 是外部可观察、不可直接修改的状态。

UI 只能观察 `state`，不能直接改它。

### 第二，所有用户操作统一走 `dispatch`

```kotlin
fun dispatch(intent: CounterIntent)
```

`dispatch` 可以理解成“派发事件”。UI 不再调用 `increment()`、`decrement()`、`reset()` 三个不同函数，而是统一发送 `CounterIntent`。

这就是 MVI 的味道：

```text
UI 不直接说“我要调用加一函数”
UI 只说“用户产生了一个 Increment 意图”
```

然后 ViewModel 再统一处理。

### 第三，Effect 用 Channel 发送

```kotlin
private val effectChannel = Channel<CounterEffect>(Channel.BUFFERED)
val effects = effectChannel.receiveAsFlow()
```

这里用 `Channel` 存一次性事件，再转成 Flow 给 UI 收集。

当前项目里，点击 Reset 后会发送：

```kotlin
CounterEffect.ShowMessage("Counter reset")
```

UI 收到后弹出 Snackbar。

## 6. `CounterScreen`：显示 State，发送 Intent，收 Effect

核心函数：

```kotlin
@Composable
fun CounterScreen(
    state: CounterState,
    effects: Flow<CounterEffect>,
    dispatch: (CounterIntent) -> Unit,
) {
    ...
}
```

它接收三样东西：

- `state`：当前页面状态；
- `effects`：一次性事件流，比如弹提示；
- `dispatch`：发送用户意图的函数。

按钮点击时：

```kotlin
Button(onClick = { dispatch(CounterIntent.Decrement) }) { Text("-1") }
Button(onClick = { dispatch(CounterIntent.Increment) }) { Text("+1") }
TextButton(onClick = { dispatch(CounterIntent.Reset) }) { Text("Reset") }
```

这非常 MVI：

> UI 不关心具体怎么改状态，只负责把用户动作翻译成 Intent。

### `LaunchedEffect` 在做什么？

```kotlin
LaunchedEffect(effects) {
    effects.collect { effect ->
        when (effect) {
            is CounterEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message)
        }
    }
}
```

这段是在 Compose 中收集一次性事件。

`effects.collect { ... }` 表示不断接收 Effect。

如果收到 `ShowMessage`，就显示 Snackbar。

`LaunchedEffect` 可以先简单理解成：

> 在 Compose 页面里启动一段协程，用来处理这种“不是直接画 UI，但和 UI 生命周期有关”的事情。

## 7. `MainActivity`：把 ViewModel 和页面接起来

```kotlin
val viewModel: CounterViewModel = viewModel()
val state by viewModel.state.collectAsStateWithLifecycle()
CounterScreen(
    state = state,
    effects = viewModel.effects,
    dispatch = viewModel::dispatch,
)
```

这里做了三件事：

1. 获取 ViewModel；
2. 观察 ViewModel 的 `state`；
3. 把 `state`、`effects`、`dispatch` 传给页面。

所以 Activity 自己不处理计数逻辑。它更像接线员：

```text
ViewModel 的状态 → 接给 CounterScreen
CounterScreen 的 Intent → 接回 ViewModel
```

## 8. `CounterRepository`：提供计数计算规则

```kotlin
interface CounterRepository {
    fun applyDelta(value: Int, delta: Int): Int
}

class InMemoryCounterRepository : CounterRepository {
    override fun applyDelta(value: Int, delta: Int) = value + delta
}
```

这个 Repository 比 MVVM 版更通用：它不单独写 `increment` / `decrement`，而是提供 `applyDelta`。

`delta` 表示变化量：

- `1` 表示加一；
- `-1` 表示减一。

当前逻辑很小，但保留 Repository 层可以帮助你理解真实项目里的数据层位置。

## 9. 这个项目里的重要 Kotlin 语法

### `sealed interface`

适合表达“有限的几种类型”。

```kotlin
sealed interface CounterIntent
```

它让编译器知道 `CounterIntent` 目前有哪些可能。

### `data object`

适合表达“不携带额外数据的事件”。

```kotlin
data object Reset : CounterIntent
```

### `data class`

适合表达“携带数据的状态或事件”。

```kotlin
data class CounterState(val count: Int = 0)
data class ShowMessage(val message: String) : CounterEffect
```

### `when`

适合根据不同 Intent 做不同处理。

```kotlin
when (intent) {
    CounterIntent.Increment -> ...
    CounterIntent.Decrement -> ...
    CounterIntent.Reset -> ...
}
```

### `is`

用于判断某个对象是不是某种类型。

```kotlin
is CounterEffect.ShowMessage -> ...
```

### 函数类型

```kotlin
dispatch: (CounterIntent) -> Unit
```

表示 `dispatch` 是一个函数：接收一个 `CounterIntent`，没有返回值。

### 函数引用

```kotlin
dispatch = viewModel::dispatch
```

表示把 ViewModel 的 `dispatch` 函数传给 UI，等 UI 点击按钮时再调用。

## 10. 测试文件在测什么？

`CounterReducerTest.kt` 测的是 Reducer：

```kotlin
val incremented = reducer.reduce(CounterState(), CounterIntent.Increment)
val decremented = reducer.reduce(incremented, CounterIntent.Decrement)
val reset = reducer.reduce(CounterState(7), CounterIntent.Reset)
```

它直接给 Reducer 输入旧状态和 Intent，然后检查输出状态是否正确。

这也是 MVI 的一个优点：

> Reducer 很像纯函数，输入明确、输出明确，因此特别适合测试。

## 11. MVI 数据流完整走一遍

以点击 `+1` 为例：

```text
用户点击 +1 按钮
→ CounterScreen 调用 dispatch(CounterIntent.Increment)
→ CounterViewModel.dispatch 收到 Intent
→ CounterReducer.reduce 根据旧 State 算出新 State
→ _state 更新
→ Compose 观察到 state 变化
→ CounterScreen 重新显示新的 count
```

以点击 `Reset` 为例：

```text
用户点击 Reset
→ UI 发送 CounterIntent.Reset
→ Reducer 把 State 重置为 CounterState()
→ ViewModel 额外发送 CounterEffect.ShowMessage("Counter reset")
→ CounterScreen 收到 Effect
→ Snackbar 显示 Counter reset
```

## 12. MVVM 和 MVI 在这个项目里的直观区别

MVVM 版里，UI 拿到的是多个回调：

```kotlin
onIncrement = viewModel::increment
onDecrement = viewModel::decrement
onReset = viewModel::reset
```

MVI 版里，UI 只拿到一个统一入口：

```kotlin
dispatch = viewModel::dispatch
```

然后所有操作都变成 Intent：

```kotlin
dispatch(CounterIntent.Increment)
dispatch(CounterIntent.Decrement)
dispatch(CounterIntent.Reset)
```

所以可以粗略理解为：

```text
MVVM：UI 调用 ViewModel 暴露的具体方法
MVI：UI 发送 Intent，ViewModel 统一分发处理
```

## 13. 推荐阅读顺序

建议按这个顺序读：

1. `CounterContract.kt`：先认识 State、Intent、Effect；
2. `CounterScreen.kt`：看 UI 如何显示 State、发送 Intent；
3. `CounterReducer.kt`：看 Intent 如何变成新 State；
4. `CounterViewModel.kt`：看 dispatch、StateFlow、Effect 如何串起来；
5. `MainActivity.kt`：看 Activity 如何接线；
6. `CounterReducerTest.kt`：看 Reducer 为什么容易测试。

## 14. 用一句话总结 MVI 版

这个项目的 MVI 思路是：

> UI 只负责显示 State，并把用户操作包装成 Intent；ViewModel 统一接收 Intent；Reducer 负责计算新 State；一次性提示则通过 Effect 发给 UI。

如果你刚开始学 Kotlin，不需要立刻背下所有架构名词。先把这条单向数据流看懂：

```text
Intent → Reducer → State → UI
```

这就是这个 MVI 小项目最值得学习的东西。
