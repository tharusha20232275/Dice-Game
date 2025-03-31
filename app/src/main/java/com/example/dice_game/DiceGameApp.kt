package com.example.dice_game

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.dice_game.screens.AboutDialog
import com.example.dice_game.screens.StartScreen
import com.example.dice_game.screens.GameScreen

@Composable
fun DiceGameApp() {
    var currentScreen by rememberSaveable { mutableStateOf("start") }
    var showAbout by rememberSaveable { mutableStateOf(false) }

    if (showAbout) {
        AboutDialog(
            onDismiss = { showAbout = false }
        )
    }

    when (currentScreen) {
        "start" -> StartScreen(
            onNewGameClick = { currentScreen = "game" },
            onAboutClick = { showAbout = true }
        )
        "game" -> GameScreen(
            onBackToMenu = { currentScreen = "start" }
        )
    }
}
