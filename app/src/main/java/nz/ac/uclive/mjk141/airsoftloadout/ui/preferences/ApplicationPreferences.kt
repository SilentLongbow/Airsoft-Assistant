package nz.ac.uclive.mjk141.airsoftloadout.ui.preferences

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*
import nz.ac.uclive.mjk141.airsoftloadout.MainActivity
import nz.ac.uclive.mjk141.airsoftloadout.R
import nz.ac.uclive.mjk141.airsoftloadout.utils.NOTIFICATION_CHANNEL_ID

class ApplicationPreferences : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == activity?.getString(R.string.enable_dark_mode_pref_key)) {
            val enableDarkMode = sharedPreferences.getBoolean(key, false)
            if (enableDarkMode) {
                enableDarkMode()
            } else {
                disableDarkMode()
            }
        } else if (key == activity?.getString(R.string.disable_notifications_pref_key)) {
            val disableNotifications = sharedPreferences.getBoolean(key, false)
            if (disableNotifications) {
                val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
            } else {
                val mainActivity  = activity as MainActivity
                mainActivity.enableNotificationChannel()
            }
        }
    }

    private fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun onResume() {
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        super.onResume()
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        super.onPause()
    }
}