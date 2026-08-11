# CounterLab-Template

这是 `counter-lab` 仓库里的模板起点项目。

它来自 Android Studio 新建的单模块 Jetpack Compose 模板项目，运行后只显示一行：

```text
Hello Android!
```

这个目录的作用不是展示复杂功能，而是让初学者先看懂一个最小 Compose Android 项目的基本骨架：

- `MainActivity.kt`：应用入口；
- `setContent { ... }`：Compose UI 的入口；
- `Greeting(...)`：显示文字的 Composable；
- `ui/theme/`：颜色、字体和主题配置；
- `build.gradle.kts`：Android、Kotlin、Compose 相关构建配置。

## 学习导读

建议先读：

```text
docs/kotlin-guide-for-beginners.md
```

这份文档面向尚未接触过 Kotlin 的学生，不做机械逐行解释，而是优先讲清楚：

- App 是从哪里开始运行的；
- Kotlin 函数、参数、默认值、lambda、注解是什么；
- Compose 为什么可以用 Kotlin 函数写 UI；
- 哪些模板代码可以先略读。

## 和另外两个目录的关系

```text
CounterLab-Template
        │
        ├── CounterLab-MVVM
        └── CounterLab-MVI
```

`CounterLab-MVVM` 和 `CounterLab-MVI` 都是在这个模板项目的基础上演进出来的计数器示例。

## 构建

```bash
./gradlew testDebugUnitTest assembleDebug
```

