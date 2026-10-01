package com.example.medicarealert

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val reqNotifPerm = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* ignore */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val graph = navController.navInflater.inflate(R.navigation.nav_graph)

        val isLoggedIn = checkLoginStatus()
        val forced = intent.getStringExtra("dest")?.takeIf { it == "home" || it == "welcome" }

        val startId = when {
            forced == "home"     -> R.id.homeFragment
            forced == "welcome"  -> R.id.welcomeFragment
            isLoggedIn           -> R.id.homeFragment
            else                 -> R.id.welcomeFragment
        }
        graph.setStartDestination(startId)
        navController.graph = graph
        intent.removeExtra("dest")

        val bottom = findViewById<BottomNavigationView>(R.id.bottomNav)
        val appBarConfig = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.updatesFragment, R.id.manageFragment)
        )
        setupActionBarWithNavController(navController, appBarConfig)
        bottom.setupWithNavController(navController)

        val authScreens = setOf(R.id.welcomeFragment, R.id.signinFragment, R.id.signupFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val hideBars = destination.id in authScreens
            bottom.isVisible = !hideBars
            toolbar.isVisible = !hideBars
            if (hideBars) supportActionBar?.hide() else supportActionBar?.show()
        }

        // ---- Permissions ----
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) reqNotifPerm.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = getSystemService(AlarmManager::class.java)
            if (!am.canScheduleExactAlarms()) {
                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
        }
    }

    private fun checkLoginStatus(): Boolean {
        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        return prefs.getLong("user_id", -1L) > 0
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHost.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
