package com.itsmemal.nexusmotion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.itsmemal.nexusmotion.ui.NexusMotionApp
import com.itsmemal.nexusmotion.ui.theme.NexusMotionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(9, 13, 21)
        window.navigationBarColor = android.graphics.Color.rgb(9, 13, 21)
        setContent {
            NexusMotionTheme {
                NexusMotionApp()
            }
        }
    }
}
