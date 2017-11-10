package ns.me.ns.furaffinity.ui.activity

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ActivityMainBinding
import ns.me.ns.furaffinity.ui.viewmodel.MainViewModel


class MainActivity : AbstractBaseActivity<MainViewModel>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        (binding.imageGalleryRecyclerView.layoutManager as? GridLayoutManager)?.let {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        (binding.imageGalleryRecyclerView.adapter.itemCount - 1) -> it.spanCount
                        else -> 1
                    }
                }
            }
        }
        binding.imageGalleryRecyclerView.adapter = viewModel.imageGalleryAdapter
    }

}
