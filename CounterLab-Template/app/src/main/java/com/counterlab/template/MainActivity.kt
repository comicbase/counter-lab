package com.counterlab.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.counterlab.template.ui.theme.CounterLabTemplateTheme

// 主 Activity，应用的入口点
class MainActivity : ComponentActivity() {
    // 当 Activity 创建时调用
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 启用边缘到边缘显示
        setContent {
            CounterLabTemplateTheme { // 应用自定义主题
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding) // 应用内边距
                    )
                }
            }
        }
    }
}

// 可组合函数，用于显示问候语
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", // 显示问候文本
        modifier = modifier
    )
}

// 预览函数，用于在 Android Studio 中预览 UI
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CounterLabTemplateTheme {
        Greeting("Android")
    }
}