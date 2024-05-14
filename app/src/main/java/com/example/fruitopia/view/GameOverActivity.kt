package com.example.fruitopia.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fruitopia.MainActivity
import com.example.fruitopia.R

class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        // Retrieve the highest score from SharedPreferences
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val highestScore = sharedPref.getInt("highestScore", 0)

        // Display the highest score in the TextView
        val highestScoreTextView = findViewById<TextView>(R.id.highestScoreTextView)
        highestScoreTextView.text = "Highest Score: $highestScore"

        // Retrieve the score from Intent extras and display it
        val score = intent.getIntExtra("score", 0)
        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)
        scoreTextView.text = "Score: $score"

        // Restart button click listener
        val restartButton = findViewById<Button>(R.id.restartButton)
        restartButton.setOnClickListener {
            // Start MainActivity to restart the game
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Finish the current activity
        }

        // Clear Highest Score button click listener
        val clearHighestScoreButton = findViewById<Button>(R.id.buttonClearHighestScore)
        clearHighestScoreButton.setOnClickListener {
            clearHighestScore()
            // Optionally, inform the user that the highest score has been cleared
            Toast.makeText(this, "Highest score cleared", Toast.LENGTH_SHORT).show()
        }

    }

    private fun clearHighestScore() {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("highestScore") // Remove the highest score key
            apply()
        }
    }
}
