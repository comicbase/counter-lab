# CounterLab-MVVM Kotlin 初学者导读

这份导读面向还没有系统接触过 Kotlin 的同学。它不会把每一行代码都拆成语法词典，而是带你抓住这个 MVVM 演进项目里最重要的几个问题：

- 这个 App 打开后发生了什么；
- MVVM 在这个项目里分别对应哪些文件；
- Kotlin 里哪些语法是你必须先看懂的；
- Compose、ViewModel、StateFlow 是怎样配合起来的。

`CounterLab-MVVM` 是从 `CounterLab-Template` 这个 Android Studio 单模块 Jetpack Compose 模板项目演进来的。模板项目原本只显示 `Hello Android!`，这个版本把它演进成了一个小计数器页面：页面上有 `+1`、`-1`、`Reset` 三个操作。

## 1. 先用一句话看懂这个项目

这个 MVVM 版本的核心可以这样理解：

> 用户点击按钮，UI 把操作交给 `CounterViewModel`；`CounterViewModel` 修改 `CounterUiState`；Compose 观察到状态变化后，自动刷新页面。

它不是靠你手动找 TextView 再设置文字，而是靠“状态变了，界面跟着变”。

## 2. MVVM 在本项目里分别是谁？

本项目的关键文件是：

```text
CounterLab-MVVM/
└── app/src/main/java/com/example/mydemo1mvvm/
    ├── MainActivity.kt              # Activity 宿主，连接 ViewModel 和 Compose 页面
    ├── ui/
    │   ├── CounterScreen.kt         # View：无状态 Composable，只显示状态和暴露事件
    │   ├── CounterViewModel.kt      # ViewModel：持有 StateFlow，更新 CounterUiState
    │   └── theme/                   # Compose 主题
    └── data/
        └── CounterRepository.kt     # Repository 接口 + InMemory 实现
```

可以先按这个关系理解：

```text
View
└── CounterScreen.kt
    负责显示页面，按钮被点击时调用外部传进来的函数

ViewModel
└── CounterViewModel.kt
    负责保存页面状态，并提供 increment / decrement / reset 操作

Model
└── CounterRepository.kt
    负责计数规则，比如 +1、-1、归零

Activity
└── MainActivity.kt
    负责把 View 和 ViewModel 接起来
```

如果你刚学架构，先记住一个朴素版本：

> View 负责“长什么样”，ViewModel 负责“现在是什么状态”，Model/Repository 负责“数据怎么来、怎么算”。

## 3. 从 `MainActivity` 看应用如何启动

看这段：

```kotlin
setContent {
    MyDemo1MvvmTheme {
        val viewModel: CounterViewModel = viewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        CounterScreen(
            uiState = uiState,
            onIncrement = viewModel::increment,
            onDecrement = viewModel::decrement,
            onReset = viewModel::reset,
        )
    }
}
```

它的意思是：

1. 用 `setContent` 开始写 Compose 页面；
2. 套上项目主题 `MyDemo1MvvmTheme`；
3. 创建或取得一个 `CounterViewModel`；
4. 从 ViewModel 里观察 `uiState`；
5. 把状态和按钮事件都传给 `CounterScreen`。

这里初学者最该关注的是这两行：

```kotlin
val viewModel: CounterViewModel = viewModel()
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

第一行取得 ViewModel。你可以把 ViewModel 理解成“页面背后的状态管理员”。

第二行把 ViewModel 里的 `StateFlow` 转成 Compose 可以直接使用的状态。状态一变化，Compose 会重新绘制相关 UI。

## 4. `CounterScreen`：只负责显示，不负责算数据

`CounterScreen.kt` 里有这个函数：

```kotlin
@Composable
fun CounterScreen(
    uiState: CounterUiState,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
) {
    ...
}
```

这是一个 Compose 页面函数。它接收四个东西：

- `uiState`：当前页面状态，比如当前计数是多少；
- `onIncrement`：点击 `+1` 时要做什么；
- `onDecrement`：点击 `-1` 时要做什么；
- `onReset`：点击 `Reset` 时要做什么。

注意，`CounterScreen` 自己并不知道“怎么 +1”。它只是说：

> 如果用户点了 +1，我就调用 `onIncrement`。

这就是 MVVM 里 View 的一个好习惯：View 尽量简单，只显示状态、把用户操作往外抛。

### `() -> Unit` 是什么意思？

```kotlin
onIncrement: () -> Unit
```

这是 Kotlin 的函数类型。

可以读作：

> `onIncrement` 是一个函数，它没有参数，也没有有意义的返回值。

所以按钮里可以这样写：

```kotlin
Button(onClick = onIncrement) { Text("+1") }
```

意思就是：按钮被点击时，执行 `onIncrement`。

## 5. `CounterUiState`：把页面状态装进一个对象里

在 `CounterViewModel.kt` 中：

```kotlin
data class CounterUiState(val count: Int = 0)
```

这行很重要。

`data class` 是 Kotlin 很适合表达“数据”的类。它会自动帮你生成很多常用能力，比如比较两个对象是否相等、复制对象等。

这里的 `CounterUiState` 表示页面状态，目前只有一个字段：

```kotlin
count: Int = 0
```

意思是计数值，默认是 0。

为什么不直接用一个 `Int`？因为真实项目里的页面状态通常不止一个字段。以后可能会变成：

```kotlin
data class CounterUiState(
    val count: Int = 0,
    val title: String = "MVVM Counter",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
```

所以一开始就把 UI 状态包装成 `UiState`，是很常见的工程习惯。

## 6. `CounterViewModel`：页面状态管理员

核心代码如下：

```kotlin
class CounterViewModel(
    private val repository: CounterRepository = InMemoryCounterRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() = _uiState.update { it.copy(count = repository.increment(it.count)) }
    fun decrement() = _uiState.update { it.copy(count = repository.decrement(it.count)) }
    fun reset() = _uiState.update { it.copy(count = repository.reset()) }
}
```

先看类声明：

```kotlin
class CounterViewModel(...) : ViewModel()
```

这表示 `CounterViewModel` 继承 AndroidX 的 `ViewModel`。ViewModel 的特点是：它比普通 UI 函数更适合保存页面状态，屏幕旋转等情况下也更稳定。

### 为什么有 `_uiState` 和 `uiState` 两个变量？

```kotlin
private val _uiState = MutableStateFlow(CounterUiState())
val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()
```

这是非常经典的写法。

可以这样理解：

- `_uiState`：内部可修改版本，只有 ViewModel 自己能改；
- `uiState`：外部只读版本，UI 只能观察，不能乱改。

这有点像店铺柜台：

```text
ViewModel 后厨：可以改状态
UI 前台：只能看状态，然后展示给用户
```

`private` 表示私有，只能在当前类里访问。

### `MutableStateFlow` 是什么？

`MutableStateFlow` 可以理解成“会通知别人的状态容器”。

当里面的值变化时，正在观察它的 Compose UI 会收到通知，然后刷新界面。

### `update { it.copy(...) }`

看这行：

```kotlin
fun increment() = _uiState.update { it.copy(count = repository.increment(it.count)) }
```

初学者可能会被它吓到，我们拆开看。

`fun increment()` 定义了一个函数，表示执行加一操作。

`_uiState.update { ... }` 表示更新当前状态。

lambda 里的 `it` 代表“当前旧状态”。

`it.copy(...)` 表示基于旧状态复制一个新状态，只修改其中的 `count`。

所以这行翻译成人话就是：

> 拿到当前状态，把 count 交给 repository 算出新值，再复制出一个新的 UI 状态。

这里体现了现代 UI 开发常见的思想：不要到处直接改对象，而是生成一个新的状态。

## 7. `CounterRepository`：把数据规则放到单独地方

```kotlin
interface CounterRepository {
    fun increment(value: Int): Int
    fun decrement(value: Int): Int
    fun reset(): Int
}

class InMemoryCounterRepository : CounterRepository {
    override fun increment(value: Int) = value + 1
    override fun decrement(value: Int) = value - 1
    override fun reset() = 0
}
```

`interface` 是接口，表示“我规定你应该有什么能力”。

这里规定一个计数仓库应该能：

- 加一；
- 减一；
- 重置。

`InMemoryCounterRepository` 是具体实现。`InMemory` 表示当前只在内存里计算，不涉及数据库、网络或文件。

为什么这么小的逻辑也要放 Repository？教学项目里这样写，是为了让你看到架构分层的样子。真实项目里 Repository 往往会负责：

- 请求接口；
- 读写数据库；
- 管理缓存；
- 合并多个数据来源。

## 8. 这个项目里的重要 Kotlin 语法

### `data class`

```kotlin
data class CounterUiState(val count: Int = 0)
```

适合表示“状态数据”。它简洁、可比较、可复制。

### `interface`

```kotlin
interface CounterRepository
```

接口只描述能力，不关心具体怎么做。

### 默认参数

```kotlin
private val repository: CounterRepository = InMemoryCounterRepository()
```

如果创建 ViewModel 时没有传 Repository，就默认使用内存版 Repository。

### 函数引用

```kotlin
onIncrement = viewModel::increment
```

`::increment` 表示“把这个函数本身传过去”。它不是立刻执行，而是把函数交给按钮，等点击时再执行。

### `by`

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

这里的 `by` 是 Kotlin 委托语法。初学阶段可以先粗略理解成：让 `uiState` 直接拿到状态值，而不是每次写 `.value`。

### `private`

```kotlin
private val _uiState = ...
```

表示只能在类内部使用。用它可以保护状态不被外部随便修改。

## 9. 测试文件在测什么？

`CounterViewModelTest.kt` 里：

```kotlin
val viewModel = CounterViewModel()
viewModel.increment()
viewModel.increment()
viewModel.decrement()
assertEquals(1, viewModel.uiState.value.count)
viewModel.reset()
assertEquals(0, viewModel.uiState.value.count)
```

这个测试没有启动模拟器，也没有点真实按钮。它直接创建 ViewModel，然后调用函数，检查状态是否符合预期。

这正是 MVVM 的好处之一：

> 业务状态逻辑放在 ViewModel 里后，不依赖 UI，也更容易测试。

## 10. 推荐阅读顺序

建议按这个顺序读：

1. `CounterScreen.kt`：先看页面长什么样；
2. `CounterViewModel.kt`：再看状态怎么变化；
3. `CounterRepository.kt`：看计数规则被放在哪里；
4. `MainActivity.kt`：看 Activity 如何把 View 和 ViewModel 接起来；
5. `CounterViewModelTest.kt`：最后看如何测试 ViewModel。

## 11. 用一句话总结 MVVM 版

这个项目的 MVVM 思路是：

> UI 不自己保存计数，也不自己计算加减；UI 只展示 `uiState`，点击按钮时调用 ViewModel，ViewModel 更新状态，Compose 再根据新状态刷新 UI。

如果你刚开始学 Kotlin 和 Android，这个项目最值得学的不是“计数器”本身，而是这条数据流：

```text
用户点击按钮
→ CounterScreen 调用回调
→ CounterViewModel 修改 CounterUiState
→ MainActivity/Compose 观察到新状态
→ CounterScreen 重新显示新的 count
```

看懂这条线，你就已经摸到 MVVM 的门把手了。
