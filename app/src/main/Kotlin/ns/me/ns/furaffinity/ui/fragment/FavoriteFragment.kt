package ns.me.ns.furaffinity.ui.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.FragmentFavoriteBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.activity.FullViewActivity
import ns.me.ns.furaffinity.ui.viewmodel.FavoriteViewModel
import ns.me.ns.furaffinity.ui.viewmodel.FullViewViewModel
import javax.inject.Inject

/**
 *
 */
class FavoriteFragment() : AbstractBaseFragment<FavoriteViewModel>(), Injectable {

    companion object {
        fun instance() = FavoriteFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: FavoriteViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FavoriteViewModel::class.java)
    }

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.recyclerView.adapter = viewModel.favoriteAdapter
        viewModel.adapterOnItemClickSubject.subscribe {
            startActivity(FullViewActivity.intent(activity, FullViewViewModel.Type.Favorite, it.data.viewId),
                    FullViewActivity.option(activity, it.view)
            )
        }.also { disposer.add(it) }

    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshAdapter()
    }

}