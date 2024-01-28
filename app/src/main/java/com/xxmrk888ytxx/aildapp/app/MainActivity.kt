package com.xxmrk888ytxx.aildapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xxmrk888ytxx.aildapp.presentation.RecordScreen
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity() {
    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinAndroidContext {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.RecordScreen.route) {
                    composable(Screen.RecordScreen.route) {
                        RecordScreen()
                    }
                }
            }
        }
    }
}