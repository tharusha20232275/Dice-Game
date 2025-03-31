package com.example.dice_game.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * StartScreen: The first screen of the app.
 * Shows "New Game" and "About" buttons.
 * - onNewGameClick: Callback to navigate to the GameScreen.
 * - onAboutClick: Callback to show AboutDialog.
 */
@Composable
fun StartScreen(
    onNewGameClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Dice Game",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNewGameClick) {
            Text("New Game")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAboutClick) {
            Text("About")
        }
    }
}
