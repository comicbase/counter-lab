# CounterLab-MVI

这是 `compose-counter-architecture-lab` 仓库里的 MVI 演进项目。

它基于 `CounterLab-Template` 这个 Android Studio 单模块 Jetpack Compose 模板项目演进而来：模板项目原本只显示 `Hello Android!`，这个版本把它改造成一个计数器示例，并使用 MVI 的单向数据流组织代码。

## 这个项目展示什么？

页面包含三个操作：

```text
-1
+1
Reset
```

MVI 版本的核心思路是：

```text
用户点击按钮
→ CounterScreen 发送 CounterIntent
→ CounterViewModel dispatch Intent
→ CounterReducer 根据旧 CounterState 算出新 CounterState
→ Compose 观察到状态变化并刷新页面
```

`Reset` 还会额外发送一个 `CounterEffect.ShowMessage`，由 UI 显示 Snackbar。

## 关键文件

```text
CounterLab-MVI/
├── app/src/main/java/com/example/mydemo1mvi/
│   ├── MainActivity.kt              # Activity 宿主，连接 ViewModel 和 Compose 页面
│   ├── ui/
│   │   ├── CounterScreen.kt         # View：显示 State、发送 Intent、收集 Effect
│   │   ├── CounterContract.kt       # Contract：定义 State / Intent / Effect
│   │   ├── CounterReducer.kt        # Reducer：状态计算器
│   │   ├── CounterViewModel.kt      # ViewModel：dispatch Intent
│   │   └── theme/                   # Compose 主题
│   └── data/
│       └── CounterRepository.kt     # Repository 接口 + InMemory 实现
└── docs/
    └── kotlin-guide-for-beginners.md
```

## MVI 角色对应

- `CounterState`：页面长期状态，比如当前计数值。
- `CounterIntent`：用户意图，比如 `Increment`、`Decrement`、`Reset`。
- `CounterEffect`：一次性事件，比如弹 Snackbar。
- `CounterReducer`：状态计算器，根据旧 State 和 Intent 生成新 State。
- `CounterViewModel`：统一接收 Intent，更新 State，并发送 Effect。
- `CounterScreen`：View，只负责显示 State、发送 Intent、收集 Effect。
- `CounterReducerTest`：验证 Reducer 对相同输入能产生可预测状态。

## 学习导读

如果你是 Kotlin / Android 初学者，建议先读：

```text
docs/kotlin-guide-for-beginners.md
```

这份导读不会机械逐行解释，而是用更通俗的方式说明：

- `State / Intent / Effect` 分别是什么；
- `sealed interface` 和 `data object` 为什么适合表达 Intent；
- `Reducer` 为什么容易测试；
- `dispatch(CounterIntent)` 这种统一事件入口怎么读；
- MVI 的单向数据流应该按什么顺序理解。

## 构建

```bash
./gradlew testDebugUnitTest assembleDebug
```
