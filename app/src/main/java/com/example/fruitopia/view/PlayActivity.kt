package com.example.fruitopia.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.example.fruitopia.MainActivity
import com.example.fruitopia.R
import java.lang.Exception

class PlayActivity : AppCompatActivity() {
    private lateinit var startbtn:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val action = supportActionBar
        action?.hide()
        startbtn = findViewById(R.id.startbtn)
        startbtn.setOnClickListener{
            startActivity(Intent
                (this@PlayActivity,
                MainActivity::class.java))
        }

    }
}