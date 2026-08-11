# CounterLab-Template Kotlin 初学者导读

这份文档面向还没有接触过 Kotlin 的同学。它讲的是公开仓库 `compose-counter-architecture-lab` 里的模板起点项目：`CounterLab-Template`。

它不会机械地逐行解释每个 `import` 或每个括号，而是挑出读懂这个项目最必要的内容，用尽量接近课堂讲解的方式说明：

- 这个 App 是怎么启动的；
- Kotlin 代码大概长什么样；
- Compose UI 是怎么写出来的；
- 哪些语法初学者必须先看懂；
- 哪些代码暂时可以先跳过。

`CounterLab-Template` 是一个从 Android Studio 新建的单模块 Jetpack Compose 模板项目：运行后页面上显示一行文字 `Hello Android!`。虽然功能很少，但它已经包含了一个 Android Kotlin 项目的基本骨架，也是后续 `CounterLab-MVVM` 和 `CounterLab-MVI` 两个演进项目的起点。

## 1. 先看项目里最重要的文件

刚开始不要急着看所有文件。这个项目里最值得先看的 Kotlin 文件是：

```text
CounterLab-Template/
└── app/src/main/java/com/example/mydemo1/
    └── MainActivity.kt          # 应用入口，打开 App 后首先进入这里
```

它是应用的入口。你可以把它理解成“App 打开后的第一个房间”。

项目里还有几个主题文件：

```text
CounterLab-Template/
└── app/src/main/java/com/example/mydemo1/ui/theme/
    ├── Color.kt                 # 颜色定义
    ├── Type.kt                  # 字体排版
    └── Theme.kt                 # Compose 主题入口
```

它们负责颜色、字体、主题风格。初学阶段不需要一上来就完全吃透，知道它们是在“给页面换皮肤”就够了。

另外还有两个测试文件：

```text
CounterLab-Template/
└── app/src/
    ├── test/java/com/example/mydemo1/
    │   └── ExampleUnitTest.kt           # 本地 JVM 单元测试
    └── androidTest/java/com/example/mydemo1/
        └── ExampleInstrumentedTest.kt   # 运行在模拟器/真机上的仪器测试
```

它们是 Android Studio 模板自动生成的示例测试。`CounterLab-Template` 还没有真实业务，所以这两个测试暂时不是重点。

## 2. App 是从哪里开始运行的？

看 `MainActivity.kt` 中这段代码：

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDemo1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
```

先不要被里面的函数名吓到。整体可以翻译成一句人话：

> 当 `MainActivity` 被打开时，系统调用 `onCreate`，然后我们用 `setContent` 放入 Compose 页面内容。

这里有几个重要概念。

### `class MainActivity : ComponentActivity()`

这行是在定义一个类：

```kotlin
class MainActivity : ComponentActivity()
```

可以理解为：

> 创建一个叫 `MainActivity` 的页面，它继承自 Android 提供的 `ComponentActivity`。

Kotlin 里用 `:` 表示继承。类似 Java 里的 `extends`。

### `override fun onCreate(...)`

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
```

这行是在重写 Android 生命周期方法。简单说，`onCreate` 会在页面创建时被系统自动调用。

几个词拆开看：

- `override`：我正在重写父类已有的方法；
- `fun`：我要定义一个函数；
- `onCreate`：函数名；
- `Bundle?`：参数类型，后面的 `?` 表示这个值可能为空。

初学 Kotlin 时，看到 `?` 要特别敏感。它表示“这个变量有可能是 null”。Kotlin 很重视空值安全，所以会把“可能为空”和“不允许为空”明确区分开。

### `setContent { ... }`

```kotlin
setContent {
    ...
}
```

这是 Compose 项目最关键的入口之一。

传统 Android 可能会写 XML 布局，而 Compose 是直接用 Kotlin 写 UI。`setContent` 的意思就是：

> 我要开始用 Kotlin 描述这个页面上显示什么。

后面的大括号 `{ ... }` 是 Kotlin 的 lambda。初学可以先把它理解成“一段可以传进去执行的代码”。

## 3. 页面上那句 Hello Android 是怎么来的？

继续看下面这段：

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
```

这是项目中真正显示文字的地方。

### `@Composable` 是什么？

```kotlin
@Composable
```

这个注解表示：

> 下面这个函数不是普通函数，它是一个可以用来画 UI 的 Compose 函数。

在 Compose 里，一个按钮、一个文本、一整块页面，都可以写成 `@Composable` 函数。

初学时可以粗略理解为：

> 只要函数前面有 `@Composable`，它就可能是在描述界面。

### `fun Greeting(...)`

```kotlin
fun Greeting(name: String, modifier: Modifier = Modifier)
```

这是定义一个函数，名字叫 `Greeting`。

它接收两个参数：

```kotlin
name: String
modifier: Modifier = Modifier
```

第一个参数 `name: String` 很好理解：传进来一个字符串。

第二个参数：

```kotlin
modifier: Modifier = Modifier
```

稍微有点 Compose 味道。`Modifier` 可以理解为“修饰器”，用来控制 UI 的大小、边距、点击、背景等。

后面的 `= Modifier` 表示默认值。也就是说，调用 `Greeting` 时可以不传 `modifier`，它会自动使用默认的空修饰器。

### `"Hello $name!"`

```kotlin
text = "Hello $name!"
```

这是 Kotlin 的字符串模板。

如果 `name` 的值是 `"Android"`，那么：

```kotlin
"Hello $name!"
```

最终就是：

```text
Hello Android!
```

这比字符串拼接更直观。Java 里可能会写：

```java
"Hello " + name + "!"
```

Kotlin 里常写成：

```kotlin
"Hello $name!"
```

## 4. Compose 页面是怎么一层层包起来的？

`setContent` 里面有这样一段：

```kotlin
MyDemo1Theme {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
```

可以把它想成三层结构：

```text
MyDemo1Theme       主题：颜色、字体、Material 风格
└── Scaffold       页面脚手架：给页面提供基础结构
    └── Greeting   真正显示 Hello Android 的组件
```

### `MyDemo1Theme { ... }`

这是项目自己的主题。它决定页面使用什么颜色、字体和 Material3 风格。

暂时可以理解成：

> 给里面的 UI 套一层统一皮肤。

### `Scaffold(...) { innerPadding -> ... }`

`Scaffold` 是 Material3 提供的页面脚手架。

在更复杂的页面里，它可以放：

- 顶部标题栏；
- 底部导航栏；
- 悬浮按钮；
- 页面主体内容。

当前项目没有这些复杂内容，只是用它包了一下 `Greeting`。

这里的：

```kotlin
{ innerPadding -> ... }
```

表示这个 lambda 接收一个参数 `innerPadding`。这个参数通常用来避免内容被状态栏、导航栏、顶部栏等遮挡。

### `Modifier.fillMaxSize()` 和 `Modifier.padding(...)`

```kotlin
Modifier.fillMaxSize()
```

表示尽量占满父容器。

```kotlin
Modifier.padding(innerPadding)
```

表示给内容加上内边距。

Compose 里经常看到 `Modifier.xxx().yyy().zzz()` 这样的链式写法。它像是在给 UI 一层层贴标签：

```text
这个组件要占满空间；
这个组件要有内边距；
这个组件要能点击；
这个组件要有背景色；
...
```

当前项目只用到了尺寸和内边距。

## 5. 初学者需要优先掌握的 Kotlin 语法

这个项目虽然小，但已经出现了不少 Kotlin 的核心语法。建议按下面顺序理解。

### 1）包名和导入

文件开头通常会看到：

```kotlin
package com.example.mydemo1

import androidx.compose.material3.Text
```

`package` 表示当前文件属于哪个包。

`import` 表示我要使用别的地方定义好的类或函数。

初学阶段不需要背 `import`。Android Studio 通常会帮你自动导入。你只要知道：代码能直接写 `Text`，是因为前面导入了它。

### 2）变量：`val`

主题文件里有很多这样的代码：

```kotlin
val Purple80 = Color(0xFFD0BCFF)
```

`val` 表示只读变量，赋值后不能再改。

可以粗略类比为 Java 的 `final` 变量。

当前项目里的颜色值、字体配置，大多都用 `val`，因为这些配置通常不希望运行时随便被改掉。

### 3）函数：`fun`

Kotlin 用 `fun` 定义函数：

```kotlin
fun Greeting(name: String) {
    ...
}
```

格式是：

```text
fun 函数名(参数名: 参数类型) {
    函数内容
}
```

如果函数没有明确返回值，它默认返回 `Unit`。`Unit` 可以理解成“没有有意义的返回值”，类似 Java 的 `void`。

### 4）默认参数

```kotlin
modifier: Modifier = Modifier
```

这表示调用函数时，`modifier` 可以不传。

例如：

```kotlin
Greeting("Android")
```

等价于：

```kotlin
Greeting(
    name = "Android",
    modifier = Modifier
)
```

默认参数是 Kotlin 很常用、也很舒服的语法。它能减少很多重载函数。

### 5）具名参数

项目里有这种写法：

```kotlin
Greeting(
    name = "Android",
    modifier = Modifier.padding(innerPadding)
)
```

这叫具名参数。好处是代码更像说明书：

```text
name 传 Android；
modifier 传 padding 后的 Modifier。
```

当参数比较多时，具名参数非常有助于阅读。

### 6）可空类型：`?`

```kotlin
savedInstanceState: Bundle?
```

`Bundle?` 表示这个值可能是 `Bundle`，也可能是 `null`。

Kotlin 的一个重要设计就是：把空值风险写在类型上。

如果一个类型后面没有 `?`，比如：

```kotlin
name: String
```

那它默认不能为 `null`。

### 7）lambda：大括号里的代码块

项目里有很多这种写法：

```kotlin
setContent {
    MyDemo1Theme {
        Greeting("Android")
    }
}
```

大括号 `{ ... }` 在这里不是普通代码块，而是传给函数的一段代码，也就是 lambda。

Compose 很喜欢这种写法，因为 UI 本身就是一层包一层：

```text
主题里面放页面；
页面里面放文本；
文本显示具体内容。
```

### 8）注解：`@Composable`、`@Preview`

```kotlin
@Composable
fun Greeting(...) { ... }
```

`@Composable` 告诉 Compose 编译器：这个函数可以描述 UI。

```kotlin
@Preview(showBackground = true)
```

`@Preview` 告诉 Android Studio：这个函数可以拿来做设计预览。

注解本身不会像普通代码那样“执行一行逻辑”，它更像是给编译器、框架或工具看的标记。

## 6. 主题文件先看懂大意即可

主题相关代码在 `ui/theme` 目录下。

### `Color.kt`

这个文件定义颜色：

```kotlin
val Purple80 = Color(0xFFD0BCFF)
val Purple40 = Color(0xFF6650a4)
```

`0xFFD0BCFF` 是十六进制颜色值。前两位 `FF` 通常表示不透明度，后面是颜色。

初学阶段只要知道：

> 这里是在给 App 准备颜色。

### `Type.kt`

这个文件定义字体排版：

```kotlin
val Typography = Typography(
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
```

这里的 `16.sp`、`24.sp` 是字体单位。`sp` 是 Android 中常用的文字大小单位，会考虑用户系统字体大小设置。

初学阶段只要知道：

> 这里是在配置文字长什么样。

### `Theme.kt`

这个文件把颜色和字体组合成主题：

```kotlin
MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
)
```

简单理解：

> `MaterialTheme` 负责把颜色、字体等规则传给内部所有 Compose UI。

里面还有一段：

```kotlin
val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> { ... }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
}
```

这里用到了 Kotlin 的 `when` 表达式。它类似更强大的 `if / else if / else`：

- 如果支持动态颜色，就使用系统动态颜色；
- 否则如果是深色模式，就用暗色主题；
- 否则用亮色主题。

这段代码初学时不必完全展开，知道它是在“根据系统状态选择颜色方案”就可以。

## 7. Gradle Kotlin DSL 简单认识

项目里还有这些 `.gradle.kts` 文件：

```text
build.gradle.kts
settings.gradle.kts
app/build.gradle.kts
```

它们不是 App 运行时逻辑，而是“构建配置”。也就是告诉 Gradle：

- 这是一个 Android 应用；
- 使用 Kotlin 和 Compose；
- 编译 SDK 是多少；
- 依赖哪些库；
- 怎么运行测试。

例如 `app/build.gradle.kts` 中：

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}
```

表示这个模块使用 Android 应用插件和 Kotlin Compose 插件。

再看：

```kotlin
android {
    namespace = "com.example.mydemo1"
    defaultConfig {
        applicationId = "com.example.mydemo1"
        minSdk = 36
        targetSdk = 36
    }
}
```

这段是在配置 Android 应用的基本信息。

还有：

```kotlin
dependencies {
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
}
```

这表示项目依赖 Compose Material3、Activity Compose 等库。

初学阶段对 Gradle 的要求很简单：

> 能看懂它是在配置插件、Android 参数和依赖即可，不需要马上掌握所有细节。

## 8. 测试文件现在可以先了解

本地单元测试：

```kotlin
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
```

它只是验证 `2 + 2 == 4`，属于模板示例。

Android 仪器测试：

```kotlin
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.mydemo1", appContext.packageName)
    }
}
```

它会在模拟器或真机上运行，检查应用包名是否正确。

`CounterLab-Template` 还没有业务逻辑，所以测试不是学习重点。等项目演进到 MVVM 或 MVI 后，测试才会更有意义，比如测试 ViewModel 状态变化、Reducer 是否正确等。

## 9. 初学者推荐阅读顺序

建议按这个顺序读项目：

1. 先看 `MainActivity.kt`，理解 App 打开后显示了什么；
2. 再看 `Greeting` 函数，理解 Compose 是用函数描述 UI；
3. 然后看 `MyDemo1Theme`，知道主题是怎么包住页面的；
4. 简单扫一眼 `Color.kt` 和 `Type.kt`，知道颜色和字体从哪里来；
5. 最后再看 `app/build.gradle.kts`，知道依赖和 Android 配置在哪里。

不要一开始就陷进所有 `import`、测试模板、Gradle 细节里。那样很容易把一个本来很小的项目看得特别复杂。

## 10. 用一句话总结这个项目

这个项目的核心逻辑其实可以压缩成一句话：

> Android 打开 `MainActivity`，`MainActivity` 用 Compose 的 `setContent` 设置页面内容，页面套上 `MyDemo1Theme`，最后通过 `Greeting("Android")` 显示 `Hello Android!`。

也就是说，`CounterLab-Template` 还不是一个复杂架构项目，而是一个适合入门 Kotlin + Compose 的最小模板。

后续如果向 MVVM 或 MVI 演进，新的学习重点会变成：

- 状态如何保存；
- 用户操作如何传递；
- UI 如何根据状态自动刷新；
- ViewModel、State、Intent、Reducer 等角色如何协作。

但在 `CounterLab-Template` 里，最重要的是先把 Kotlin 函数、参数、lambda、注解和 Compose 的 UI 组合方式看明白。
