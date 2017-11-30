package ns.me.ns.furaffinity.ui.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.FragmentBrowseBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.activity.FullViewActivity
import ns.me.ns.furaffinity.ui.viewmodel.BrowseViewModel
import ns.me.ns.furaffinity.ui.viewmodel.FullViewViewModel
import javax.inject.Inject

/**
 * Browse Fragment
 */
class BrowseFragment : AbstractBaseFragment<BrowseViewModel>(), Injectable {

    companion object {
        fun instance() = BrowseFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: BrowseViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(BrowseViewModel::class.java)
    }

    private lateinit var binding: FragmentBrowseBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_browse, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        (binding.recyclerView.layoutManager as? GridLayoutManager)?.let {
            viewModel.browseAdapter.determinationCellHeight(activity, it.spanCount)
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                        if (viewModel.browseAdapter.isHeader(position)) it.spanCount else 1
            }
        }
        binding.recyclerView.adapter = viewModel.browseAdapter

        viewModel.adapterOnItemClickSubject.subscribe {
            startActivity(FullViewActivity.intent(activity, FullViewViewModel.Type.Browse, it.data.viewId),
                    FullViewActivity.option(activity, it.view)
            )
        }.also { disposer.add(it) }

        viewModel.displayUIErrorSubject.subscribe {
            Snackbar.make(binding.swipeRefreshLayout, it.messageRes, Snackbar.LENGTH_SHORT).show()
        }.also { disposer.add(it) }

    }

}