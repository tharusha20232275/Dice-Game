package com.example.dice_game.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * SetTargetDialog: Allows the user to input a new target score (default is 101).
 * If invalid input is entered, it just dismisses or you can add validation.
 */
@Composable
fun SetTargetDialog(
    onDismiss: () -> Unit,
    onSetTarget: (Int) -> Unit
) {
    var input by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set New Target") },
        text = {
            Column {
                Text("Enter a new target score:")
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Target (e.g., 101)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newValue = input.toIntOrNull()
                if (newValue != null && newValue > 0) {
                    onSetTarget(newValue)
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
