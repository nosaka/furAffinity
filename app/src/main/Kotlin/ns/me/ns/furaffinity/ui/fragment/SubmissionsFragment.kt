package ns.me.ns.furaffinity.ui.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.FragmentSubmissionsBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.activity.FullViewActivity
import ns.me.ns.furaffinity.ui.viewmodel.SubmissionsViewModel
import ns.me.ns.furaffinity.util.BitmapUtil
import javax.inject.Inject

/**
 *
 */
class SubmissionsFragment() : AbstractBaseFragment<SubmissionsViewModel>(), Injectable {

    companion object {
        fun instance() = SubmissionsFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: SubmissionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SubmissionsViewModel::class.java)
    }

    private lateinit var binding: FragmentSubmissionsBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_submissions, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        (binding.recyclerView.layoutManager as? GridLayoutManager)?.let {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (viewModel.submissionsAdapter.isHeader(position)) it.spanCount else 1
                }
            }
        }
        binding.recyclerView.adapter = viewModel.submissionsAdapter

        viewModel.adapterOnItemClickSubject.subscribe {
            val viewId = it.data.viewId ?: return@subscribe

            val bitmap = ((it.view as? ImageView)?.drawable as? BitmapDrawable)?.bitmap
            val keyCache = bitmap?.generationId?.toString()
            keyCache?.let {
                BitmapUtil.cacheMemory(it, bitmap)
            }
            startActivity(FullViewActivity.intent(activity, viewId, keyCache),
                    FullViewActivity.option(activity, it.view)
            )
        }.also { disposer.add(it) }

    }

}