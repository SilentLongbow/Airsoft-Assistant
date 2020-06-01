package nz.ac.uclive.mjk141.airsoftloadout.ui.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import nz.ac.uclive.mjk141.airsoftloadout.R

class ApplicationPreferences : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}