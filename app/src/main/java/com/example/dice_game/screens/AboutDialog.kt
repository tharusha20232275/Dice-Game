package com.example.dice_game.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("About") },
        text = {
            Text(
                """
                Student ID: w2084406
                Name: Tharusha Kodithuwakku
                
                I confirm that I understand what plagiarism is and have read and 
                understood the section on Assessment Offences in the Essential 
                Information for Students. The work that I have submitted is 
                entirely my own. Any work from other authors is duly referenced 
                and acknowledged.
                """.trimIndent()
            )
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
