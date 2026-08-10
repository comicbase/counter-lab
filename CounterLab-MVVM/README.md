# My Demo1 MVVM

原始 Compose 模板的 MVVM 演进版本。

## 状态流

`CounterScreen → CounterViewModel → CounterRepository → StateFlow<CounterUiState> → CounterScreen`

- `CounterScreen` 是无状态 View，只渲染 `CounterUiState` 并调用明确的 ViewModel 方法。
- `CounterViewModel` 承担页面状态与业务协调。
- `CounterRepository` 隔离数据操作，当前使用内存实现，后续可替换为 Room 或网络数据源。
- `CounterViewModelTest` 验证公开操作产生的 UI 状态。

构建：`./gradlew testDebugUnitTest assembleDebug`
