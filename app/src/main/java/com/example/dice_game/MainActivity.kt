package com.example.dice_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.dice_game.ui.theme.DiceGameTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceGameTheme {
                // Launch the main composable that manages our app's screens
                DiceGameApp()
            }
        }
    }
}
