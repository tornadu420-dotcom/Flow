package com.companheirovirtual.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.companheirovirtual.app.ui.home.HomeScreen
import com.companheirovirtual.app.ui.theme.CompanheiroTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CompanheiroTheme {
                HomeScreen()
            }
        }
    }
}
