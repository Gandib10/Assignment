package com.example.rockpaperscissors

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.scale
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RockPaperScissorsGame()
        }
    }
}

@Composable
fun RockPaperScissorsGame() {
    val options = listOf("Rock", "Paper", "Scissors")
    var playerChoice by remember { mutableStateOf("") }
    var computerChoice by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Animation effect for result
    val scale by animateFloatAsState(
        targetValue = if (result.isNotEmpty()) 1.2f else 1f,
        animationSpec = tween(durationMillis = 500)
    )

    // Function to play sounds
    fun playSound(resId: Int) {
        val mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { it.release() }
    }

    fun playGame(playerMove: String) {
        playSound(R.raw.click_sound)  // Play click sound
        val computerMove = options[Random.nextInt(options.size)]
        playerChoice = playerMove
        computerChoice = computerMove

        result = when {
            playerMove == computerMove -> "It's a Draw!"
            (playerMove == "Rock" && computerMove == "Scissors") ||
                    (playerMove == "Paper" && computerMove == "Rock") ||
                    (playerMove == "Scissors" && computerMove == "Paper") -> {
                playSound(R.raw.win_sound)  // Play win sound
                "You Win!"
            }
            else -> {
                playSound(R.raw.lose_sound)  // Play lose sound
                "You Lose!"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Rock Paper Scissors", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            options.forEach { choice ->
                Button(
                    onClick = { playGame(choice) },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = choice)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your Choice: $playerChoice",
            fontSize = 18.sp,
            modifier = Modifier.scale(scale)
        )
        Text(
            text = "Computer Choice: $computerChoice",
            fontSize = 18.sp,
            modifier = Modifier.scale(scale)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = result,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = when (result) {
                "You Win!" -> Color.Green
                "You Lose!" -> Color.Red
                else -> Color.Blue
            },
            modifier = Modifier.scale(scale)
        )
    }
}
