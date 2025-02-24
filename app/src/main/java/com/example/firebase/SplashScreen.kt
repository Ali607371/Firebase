package com.example.firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebase.databinding.ActivitySplashScreenBinding


class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },3000)
        val welcometext="Welcome"
        val spanneStringable=SpannableString(welcometext)
        spanneStringable.setSpan(ForegroundColorSpan(Color.parseColor("#FB8C00")),0,5,0)
        spanneStringable.setSpan(ForegroundColorSpan(Color.parseColor("#0B0B0B")),5,welcometext.length,0)
        binding.welcome.text=spanneStringable


    }
}