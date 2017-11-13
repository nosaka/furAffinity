package ns.me.ns.furaffinity.ui.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ActivityFullViewBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.viewmodel.FullViewViewModel
import javax.inject.Inject

class FullViewActivity : AbstractBaseActivity<FullViewViewModel>(), Injectable {

    companion object {

        private const val KEY_BUNDLE_VIEW_ID = "view_id"

        fun intent(context: Context, viewId: Int) = Intent(context, FullViewActivity::class.java).apply {
            putExtra(KEY_BUNDLE_VIEW_ID, viewId)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: FullViewViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FullViewViewModel::class.java).apply {
            viewId = intent.getIntExtra(KEY_BUNDLE_VIEW_ID, -1)
        }
    }

    private lateinit var binding: ActivityFullViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_view)
        binding.viewModel = viewModel

        binding.fullImageView.transitionName = "test"
    }

}