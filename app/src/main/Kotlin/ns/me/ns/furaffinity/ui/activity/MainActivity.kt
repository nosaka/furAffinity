package ns.me.ns.furaffinity.ui.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ActivityMainBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.fragment.SubmissionsFragment
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

        replaceContainer(SubmissionsFragment.instance())

        viewModel.navigationItemSubject.subscribe {
            replaceContainer(it)
        }
    }

    private fun replaceContainer(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commitNow()
    }

}
