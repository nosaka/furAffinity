package ns.me.ns.furaffinity.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.ds.dao.AppDatabase
import ns.me.ns.furaffinity.ds.dao.FavoriteDao
import ns.me.ns.furaffinity.ds.dao.SubmissionDao
import ns.me.ns.furaffinity.util.ToastUtil


class DebugActivity : AppCompatActivity() {

    companion object {
        fun intent(context: Context) = Intent(context, DebugActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager
                .beginTransaction()
                .replace(android.R.id.content, DebugFragment())
                .commit()
    }

    class DebugFragment : PreferenceFragment() {

        private val favoriteDao: FavoriteDao by lazy {
            AppDatabase.getDatabase(activity).favoriteDao()
        }

        private val submissionDao: SubmissionDao by lazy {
            AppDatabase.getDatabase(activity).submissionDao()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.debug_preferences)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            initView()
        }

        private fun initView() {
            findPreference("deleteFavorites").setOnPreferenceClickListener {
                favoriteDao.deleteAll()
                ToastUtil.showToast(activity, "delete all Favorites.", Toast.LENGTH_SHORT)
                return@setOnPreferenceClickListener true
            }
            findPreference("deleteSubmissions").setOnPreferenceClickListener {
                submissionDao.deleteAll()
                ToastUtil.showToast(activity, "delete all Submissions.", Toast.LENGTH_SHORT)
                return@setOnPreferenceClickListener true
            }

        }
    }


}