package tang.song.edu.uwaterloo.view.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import tang.song.edu.uwaterloo.R
import tang.song.edu.uwaterloo.view.ui.fragments.HomeFragment
import tang.song.edu.uwaterloo.view.ui.fragments.SettingsFragment

private val TAB_TITLES = arrayOf(
    R.string.app_home,
    R.string.app_settings
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SettingsFragment()
            else -> HomeFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}