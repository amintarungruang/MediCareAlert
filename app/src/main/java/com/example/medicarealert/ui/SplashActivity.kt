package com.example.medicarealert.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.medicarealert.MainActivity
import com.example.medicarealert.databinding.ActivitySplashBinding
import com.example.medicarealert.data.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var vb: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(vb.root)

        lifecycleScope.launch {

            kotlinx.coroutines.delay(900)

            val session = SessionManager.getInstance(this@SplashActivity)
            val onboarded = session.isOnboarded.first()
            val isGuest   = session.isGuest.first()
            val accountId = session.accountId.first()

            val dest = when {
                onboarded && (isGuest || accountId.isNotEmpty()) -> "home"
                else -> "welcome"
            }

            val i = Intent(this@SplashActivity, MainActivity::class.java)
                .putExtra("dest", dest)
            startActivity(i)
            finish()
        }
    }
}
