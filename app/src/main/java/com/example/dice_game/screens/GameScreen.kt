package com.example.dice_game.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dice_game.DiceRow
import kotlin.random.Random

@Composable
fun GameScreen(
    onBackToMenu: () -> Unit
) {
    var humanWins by rememberSaveable { mutableStateOf(0) }
    var computerWins by rememberSaveable { mutableStateOf(0) }
    var targetScore by rememberSaveable { mutableStateOf(101) }

    var humanScore by rememberSaveable { mutableStateOf(0) }
    var computerScore by rememberSaveable { mutableStateOf(0) }

    var rollCount by rememberSaveable { mutableStateOf(0) }
    var humanDice by rememberSaveable { mutableStateOf(List(5) { 1 }) }
    var computerDice by rememberSaveable { mutableStateOf(List(5) { 1 }) }

    // Track which dice the human wants to "hold"
    var heldDice by rememberSaveable { mutableStateOf(List(5) { false }) }

    // If a winner is determined, store the message here
    var winnerMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // Dialog to set a custom target
    var showSetTargetDialog by rememberSaveable { mutableStateOf(false) }

    // Tie-break flag (if both exceed target with same score)
    var isTieBreak by rememberSaveable { mutableStateOf(false) }


    fun resetTurn() {
        rollCount = 0
        heldDice = List(5) { false }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top row: display total wins and a button to go back
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "H: $humanWins / C: $computerWins")
            Button(onClick = onBackToMenu) {
                Text("Menu")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Current game scores & target
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Target: $targetScore")
            Text("You: $humanScore - Computer: $computerScore")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to change the target
        Button(onClick = { showSetTargetDialog = true }) {
            Text("Set Target")
        }
        if (showSetTargetDialog) {
            SetTargetDialog(
                onDismiss = { showSetTargetDialog = false },
                onSetTarget = { newVal ->
                    targetScore = newVal
                    showSetTargetDialog = false
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isTieBreak) {
            Text("Tie-Breaker Round! No re-rolls allowed.")
        }

        // Human dice
        Text("Your Dice:")
        DiceRow(
            diceValues = humanDice,
            heldDice = heldDice,
            onDiceClick = { index ->
                if (!isTieBreak && rollCount in 1..2) {
                    heldDice = heldDice.toMutableList().also { it[index] = !it[index] }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Computer dice
        Text("Computer Dice:")
        DiceRow(diceValues = computerDice)

        Spacer(modifier = Modifier.height(16.dp))

        // Throw & Score buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (isTieBreak) {
                    // Single roll each, no re-roll
                    humanDice = List(5) { Random.nextInt(1, 7) }
                    computerDice = List(5) { Random.nextInt(1, 7) }
                    val hSum = humanDice.sum()
                    val cSum = computerDice.sum()
                    when {
                        hSum > cSum -> {
                            winnerMessage = "You win!"
                            humanWins++
                        }
                        cSum > hSum -> {
                            winnerMessage = "You lose!"
                            computerWins++
                        }
                        else -> return@Button // still tie => repeat
                    }
                    isTieBreak = false
                    return@Button
                }

                // Normal scenario
                if (rollCount < 3) {
                    // Human re-roll
                    humanDice = humanDice.mapIndexed { i, oldVal ->
                        if (heldDice[i]) oldVal else Random.nextInt(1, 7)
                    }
                    rollCount++

                    // Computer's roll or re-roll
                    if (rollCount == 1) {
                        // first roll
                        computerDice = List(5) { Random.nextInt(1,7) }
                    } else {
                        // subsequent re-roll
                        computerDice = computerStrategy(computerDice, rollCount)
                    }

                    // If we've reached 3 rolls, auto-score
                    if (rollCount == 3) {
                        autoScore(
                            humanDice,
                            computerDice,
                            { humanScore += it },
                            { computerScore += it }
                        )
                        resetTurn()
                        checkForWinOrTie(
                            humanScore,
                            computerScore,
                            targetScore,
                            onHumanWin = {
                                winnerMessage = "You win!"
                                humanWins++
                            },
                            onComputerWin = {
                                winnerMessage = "You lose!"
                                computerWins++
                            },
                            onTieBreak = { isTieBreak = true }
                        )
                    }
                }
            }) {
                Text("Throw")
            }

            Button(onClick = {
                if (isTieBreak) return@Button  // No "Score" in tie-break scenario

                // Score immediately
                val hSum = humanDice.sum()
                val cSum = finalizeComputerTurn(computerDice, rollCount)

                humanScore += hSum
                computerScore += cSum

                resetTurn()

                checkForWinOrTie(
                    humanScore,
                    computerScore,
                    targetScore,
                    onHumanWin = {
                        winnerMessage = "You win!"
                        humanWins++
                    },
                    onComputerWin = {
                        winnerMessage = "You lose!"
                        computerWins++
                    },
                    onTieBreak = { isTieBreak = true }
                )
            }) {
                Text("Score")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Roll Count: $rollCount")

        // Show winner popup if we have one
        if (winnerMessage != null) {
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(
                        text = if (winnerMessage == "You win!") "Congratulations!" else "Game Over"
                    )
                },
                text = {
                    Text(text = winnerMessage ?: "")
                },
                confirmButton = {
                    Button(onClick = {
                        // Clear the message
                        winnerMessage = null
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

/**
 * Computer random strategy for subsequent rolls.
 */
fun computerStrategy(currentDice: List<Int>, rollCount: Int): List<Int> {
    if (rollCount < 3) {
        return currentDice.map {
            // 50% chance to re-roll each die
            if (listOf(true, false).random()) Random.nextInt(1,7) else it
        }
    }
    return currentDice
}

/**
 * If the human hits Score before 3 rolls, the computer finishes leftover rolls automatically.
 */
fun finalizeComputerTurn(currentDice: List<Int>, rollCount: Int): Int {
    var dice = currentDice
    var rCount = rollCount
    while (rCount < 3) {
        dice = computerStrategy(dice, rCount)
        rCount++
    }
    return dice.sum()
}

/**
 * Auto-score after 3 rolls.
 */
fun autoScore(
    humanDice: List<Int>,
    computerDice: List<Int>,
    onHumanScore: (Int) -> Unit,
    onComputerScore: (Int) -> Unit
) {
    onHumanScore(humanDice.sum())
    onComputerScore(computerDice.sum())
}

/**
 * Check if we have a winner or tie after scoring.
 */
fun checkForWinOrTie(
    humanScore: Int,
    computerScore: Int,
    targetScore: Int,
    onHumanWin: () -> Unit,
    onComputerWin: () -> Unit,
    onTieBreak: () -> Unit
) {
    if (humanScore >= targetScore && computerScore >= targetScore) {
        when {
            humanScore > computerScore -> onHumanWin()
            computerScore > humanScore -> onComputerWin()
            else -> onTieBreak() // tie => tie-break
        }
    } else if (humanScore >= targetScore) {
        onHumanWin()
    } else if (computerScore >= targetScore) {
        onComputerWin()
    }
}
