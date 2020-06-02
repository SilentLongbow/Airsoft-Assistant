package nz.ac.uclive.mjk141.airsoftloadout

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import nz.ac.uclive.mjk141.airsoftloadout.utils.NOTIFICATION_CHANNEL_ID

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.main_nav_host)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (!prefs.getBoolean(getString(R.string.disable_notifications_pref_key), false)) {
            enableNotificationChannel()
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_equipment,
                R.id.navigation_map,
                R.id.navigation_preferences
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun enableNotificationChannel() {
        createNotificationChannel()
        createNotification()
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = getString(R.string.channel_description)
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification() {
        val notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID).run {
            setSmallIcon(R.drawable.ic_baseline_gun)
            setContentTitle(getString(R.string.notification_title))
            setContentText(getString(R.string.notification_text))
            build()
        }
        with(NotificationManagerCompat.from(this)) {
            notify(20, notification)
        }
    }
}
