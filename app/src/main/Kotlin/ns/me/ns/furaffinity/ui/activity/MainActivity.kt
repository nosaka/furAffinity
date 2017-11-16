package ns.me.ns.furaffinity.ui.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.transition.Fade
import android.widget.ImageView
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
        window.exitTransition = Fade()
        window.enterTransition = Fade()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        (binding.imageGalleryRecyclerView.layoutManager as? GridLayoutManager)?.let {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (viewModel.imageGalleryAdapter.isFooter(position) || viewModel.imageGalleryAdapter.isHeader(position)) it.spanCount else 1
                }
            }
        }
        binding.imageGalleryRecyclerView.adapter = viewModel.imageGalleryAdapter
        viewModel.startActivitySubject.subscribe {
            startActivity(it)
        }
        viewModel.fullViewSubject.subscribe {
            val view = it.first
            val viewId = it.second.viewId
            if (view == null || viewId == null) {
                return@subscribe
            }

            val bitmap = ((view as? ImageView)?.drawable as? BitmapDrawable)?.bitmap
            startActivity(FullViewActivity.intent(this@MainActivity, viewId, bitmap),
                    FullViewActivity.option(this@MainActivity, view)
            )

        }

    }

}
