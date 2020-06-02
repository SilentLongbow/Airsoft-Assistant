package nz.ac.uclive.mjk141.airsoftloadout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import nz.ac.uclive.mjk141.airsoftloadout.utils.SIGNED_OUT

/*
 * Activity for determining which activity to start the user
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.getDefaultSharedPreferences(this).let {
            val enableDarkMode = it.getBoolean(getString(R.string.enable_dark_mode_pref_key), false)
            if (enableDarkMode) {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            } else {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        val sharedPreferences = this.getSharedPreferences(
            packageName,
            Context.MODE_PRIVATE)

        val intent = when (sharedPreferences.getLong(getString(R.string.stored_user_id_key), SIGNED_OUT)) {
            SIGNED_OUT -> registerActivityIntent()
            else -> mainActivityIntent()
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_NO_ANIMATION

        startActivity(intent)
        finish()
    }

    private fun registerActivityIntent() = Intent(this, RegisterActivity::class.java)

    private fun mainActivityIntent() = Intent(this, MainActivity::class.java)
}
