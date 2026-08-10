# My Demo1 MVI

原始 Compose 模板的 MVI 演进版本。

## 单向状态流

`CounterScreen → CounterIntent → CounterViewModel → CounterReducer → CounterState → CounterScreen`

- View 只通过 `dispatch(CounterIntent)` 表达用户意图。
- `CounterReducer` 是纯状态转换入口，产生新的不可变 `CounterState`。
- `CounterEffect` 表达 Snackbar 等一次性副作用，避免混入持久 UI 状态。
- `CounterRepository` 隔离数据计算或数据源。
- `CounterReducerTest` 验证相同输入能产生可预测状态。

构建：`./gradlew testDebugUnitTest assembleDebug`
