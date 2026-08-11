# CounterLab-MVVM

这是 `counter-lab` 仓库里的 MVVM 演进项目。

它基于 `CounterLab-Template` 这个 Android Studio 单模块 Jetpack Compose 模板项目演进而来：模板项目原本只显示 `Hello Android!`，这个版本把它改造成一个计数器示例，并使用 MVVM 分层组织代码。

## 这个项目展示什么？

页面包含三个操作：

```text
-1
+1
Reset
```

MVVM 版本的核心思路是：

```text
用户点击按钮
→ CounterScreen 调用 ViewModel 暴露的方法
→ CounterViewModel 更新 CounterUiState
→ Compose 观察到状态变化并刷新页面
```

## 关键文件

```text
CounterLab-MVVM/
├── app/src/main/java/com/counterlab/mvvm/
│   ├── CounterLabMvvmApplication.kt   # Application 类，应用进程入口
│   ├── MainActivity.kt              # Activity 宿主，连接 ViewModel 和 Compose 页面
│   ├── ui/
│   │   ├── CounterScreen.kt         # View：无状态 Composable
│   │   ├── CounterViewModel.kt      # ViewModel：持有 StateFlow
│   │   └── theme/                   # Compose 主题
│   └── data/
│       └── CounterRepository.kt     # Repository 接口 + InMemory 实现
└── docs/
    └── kotlin-guide-for-beginners.md
```

## MVVM 角色对应

- `CounterScreen`：View，只负责显示 `CounterUiState`，并把按钮点击交给外部回调。
- `CounterViewModel`：ViewModel，负责保存页面状态，并提供 `increment()`、`decrement()`、`reset()`。
- `CounterRepository`：Model/Data 层，隔离计数规则；当前是内存实现，后续可替换为 Room、网络或其他数据源。
- `CounterViewModelTest`：验证 ViewModel 的公开操作是否能产生正确 UI 状态。

## 学习导读

如果你是 Kotlin / Android 初学者，建议先读：

```text
docs/kotlin-guide-for-beginners.md
```

这份导读不会机械逐行解释，而是用更通俗的方式说明：

- ViewModel 为什么存在；
- `CounterUiState` 是什么；
- `StateFlow` 如何让 UI 自动刷新；
- `viewModel::increment` 这种 Kotlin 函数引用怎么读；
- MVVM 项目应该按什么顺序阅读。

## 构建

```bash
./gradlew testDebugUnitTest assembleDebug
```
