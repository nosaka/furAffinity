package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.SparseArray
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.ui.fragment.BrowseFragment
import ns.me.ns.furaffinity.ui.fragment.FavoriteFragment
import ns.me.ns.furaffinity.ui.fragment.SubmissionsFragment
import javax.inject.Inject

class MainViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    val navigationItemSubject: PublishSubject<Fragment> = PublishSubject.create()

    private val fragments = SparseArray<Fragment>()

    val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {

        val itemId = it.itemId
        fragments[itemId]?.let {
            navigationItemSubject.onNext(it)
            return@OnNavigationItemSelectedListener true
        }

        val fragment: Fragment = when (it.itemId) {
            R.id.navigation_home -> {
                BrowseFragment.instance()
            }
            R.id.navigation_submissions -> {
                SubmissionsFragment.instance()
            }
            R.id.navigation_favorite -> {
                FavoriteFragment.instance()
            }
            else -> {
                return@OnNavigationItemSelectedListener false
            }
        }
        fragments.put(itemId, fragment)
        navigationItemSubject.onNext(fragment)

        return@OnNavigationItemSelectedListener true
    }

    fun getFirstFragment(): Fragment {
        val first = BrowseFragment.instance()
        fragments.put(R.id.navigation_home, first)
        return first
    }
}