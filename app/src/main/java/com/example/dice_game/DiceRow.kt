package com.example.dice_game

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * DiceRow displays a row of 5 dice images.
 * Each die is determined by diceValues[index] (1..6).
 * If heldDice[index] is true, we show a green border.
 * If onDiceClick is provided, dice are clickable (for the human player).
 */
@Composable
fun DiceRow(
    diceValues: List<Int>,
    heldDice: List<Boolean> = List(diceValues.size) { false },
    onDiceClick: ((Int) -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        diceValues.forEachIndexed { index, value ->
            val diceRes = when (value) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
            Image(
                painter = painterResource(id = diceRes),
                contentDescription = "Dice showing $value",
                modifier = Modifier
                    .size(64.dp)
                    .then(
                        if (onDiceClick != null) {
                            Modifier.clickable { onDiceClick(index) }
                        } else Modifier
                    )
                    .border(
                        width = if (heldDice[index]) 3.dp else 0.dp,
                        color = if (heldDice[index]) Color.Green else Color.Transparent,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}
