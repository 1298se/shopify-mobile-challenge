package tang.song.edu.uwaterloo.view.ui.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import tang.song.edu.uwaterloo.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
