package ns.me.ns.furaffinity.ui.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ActivityMainBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : AbstractBaseActivity<MainViewModel>(), Injectable {

    companion object {
        fun intent(context: Context) = Intent(context, MainActivity::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)

        replaceContainer(viewModel.getFirstFragment())

        viewModel.navigationItemSubject.subscribe {
            replaceContainer(it)
        }.also { disposer.add(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.debug_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.debug -> {
                startActivity(DebugActivity.intent(this@MainActivity))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceContainer(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commitNow()
    }

}
