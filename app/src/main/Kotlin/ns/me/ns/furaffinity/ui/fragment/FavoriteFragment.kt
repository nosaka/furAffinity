package ns.me.ns.furaffinity.ui.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.FragmentFavoriteBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.activity.FullViewActivity
import ns.me.ns.furaffinity.ui.viewmodel.FavoriteViewModel
import ns.me.ns.furaffinity.util.BitmapUtil
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
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.recyclerView.adapter = viewModel.favoriteAdapter
        viewModel.startActivitySubject.subscribe {
            startActivity(it)
        }
        viewModel.fullViewSubject.subscribe {
            val view = it.first
            val viewId = it.second.viewId
            if (view == null) {
                return@subscribe
            }
            val bitmap = ((view as? ImageView)?.drawable as? BitmapDrawable)?.bitmap
            val keyCache = bitmap?.generationId?.toString()
            BitmapUtil.cache(keyCache, bitmap)

            startActivity(FullViewActivity.intent(activity, viewId, keyCache),
                    FullViewActivity.option(activity, view)
            )

        }

    }


}