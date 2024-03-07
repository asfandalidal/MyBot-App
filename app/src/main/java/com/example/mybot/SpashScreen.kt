package com.example.mybot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.logging.Handler

class SpashScreen : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH = 2000 // 2 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash_screen)
            android.os.Handler().postDelayed({
                // Start main activity after delay
                startActivity(Intent(this@SpashScreen, MainActivity::class.java))
                finish()
            }, SPLASH_DISPLAY_LENGTH.toLong())
        }
    }
