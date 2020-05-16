package nz.ac.uclive.mjk141.airsoftloadout

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nz.ac.uclive.mjk141.airsoftloadout.utils.SIGNED_OUT

/*
 * Activity for determining which activity to start the user
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = this.getSharedPreferences(
            getString(R.string.stored_user_credentials),
            Context.MODE_PRIVATE)

        val intent = when (sharedPreferences.getLong(getString(R.string.stored_user_id_key),
            SIGNED_OUT)) {
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
