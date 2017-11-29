package ns.me.ns.furaffinity.ui.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ActivityGalleryBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.viewmodel.FullViewViewModel
import ns.me.ns.furaffinity.ui.viewmodel.GalleryViewModel
import javax.inject.Inject

class GalleryActivity : AbstractBaseActivity<GalleryViewModel>(), Injectable {

    companion object {

        private const val KEY_BUNDLE_ACCOUNT = "account"

        fun intent(context: Context, accountName: String) = Intent(context, GalleryActivity::class.java).apply {
            putExtra(KEY_BUNDLE_ACCOUNT, accountName)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: GalleryViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(GalleryViewModel::class.java).apply {
            account = intent?.getStringExtra(KEY_BUNDLE_ACCOUNT)
        }
    }

    val binding: ActivityGalleryBinding by lazy {
        DataBindingUtil.setContentView<ActivityGalleryBinding>(this, R.layout.activity_gallery)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        (binding.recyclerView.layoutManager as? GridLayoutManager)?.let {
            viewModel.galleryAdapter.determinationCellHeight(this@GalleryActivity, it.spanCount)
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                        if (viewModel.galleryAdapter.isHeader(position)) it.spanCount else 1
            }
        }
        binding.recyclerView.adapter = viewModel.galleryAdapter

        viewModel.adapterOnItemClickSubject.subscribe {
            val account = viewModel.account ?: return@subscribe
            startActivity(FullViewActivity.intent(this@GalleryActivity, FullViewViewModel.Type.Gallery, it.data.viewId, account),
                    FullViewActivity.option(this@GalleryActivity, it.view)
            )
        }.also { disposer.add(it) }

//        viewModel.displayUIErrorSubject.subscribe {
//            Snackbar.make(binding.swipeRefreshLayout, it.messageRes, Snackbar.LENGTH_SHORT).show()
//        }.also { disposer.add(it) }


    }


}