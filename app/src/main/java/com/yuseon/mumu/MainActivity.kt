package com.yuseon.mumu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import com.yuseon.mumu.ui.theme.MumuTheme
import com.yuseon.mumu.view.MainPageTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding()
                    .navigationBarsPadding()
            ) {
                MumuTheme {
                    MainPageTab()
                }
            }
        }
    }
}

