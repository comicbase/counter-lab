package com.example.mydemo1.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Material 排版样式
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // 默认字体
        fontWeight = FontWeight.Normal,  // 常规字重
        fontSize = 16.sp,                // 字号 16sp
        lineHeight = 24.sp,              // 行高 24sp
        letterSpacing = 0.5.sp           // 字间距 0.5sp
    )
    /* 其他可覆盖的默认文本样式
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)