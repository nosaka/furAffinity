package ns.me.ns.furaffinity.ui.activity

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.graphics.Palette
import android.view.Menu
import android.view.MenuItem
import android.view.View
import ns.me.ns.furaffinity.Constants
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.checkSelfPermission
import ns.me.ns.furaffinity.databinding.ActivityFullViewBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.viewmodel.FullViewViewModel
import javax.inject.Inject


class FullViewActivity : AbstractBaseActivity<FullViewViewModel>(), Injectable {

    companion object {

        private const val KEY_BUNDLE_TYPE = "type"

        private const val KEY_BUNDLE_VIEW_ID = "view_id"

        private const val KEY_SHARED_IMAGE = "shared_image"

        // Y軸最低スワイプ距離
        private const val FINISH_DISTANCE = -240

        fun intent(context: Context, type: FullViewViewModel.Type, viewId: Int) = Intent(context, FullViewActivity::class.java).apply {
            putExtra(KEY_BUNDLE_TYPE, type)
            putExtra(KEY_BUNDLE_VIEW_ID, viewId)
        }

        fun option(activity: Activity, view: View): Bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, KEY_SHARED_IMAGE).toBundle()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: FullViewViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FullViewViewModel::class.java).apply {
            createViewPagerItems(intent.getSerializableExtra(KEY_BUNDLE_TYPE) as FullViewViewModel.Type, intent.getIntExtra(KEY_BUNDLE_VIEW_ID, -1))
        }
    }

    private lateinit var binding: ActivityFullViewBinding

    private var favorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_view)
        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)
        binding.container.transitionName = KEY_SHARED_IMAGE

        binding.fullViewPager.adapter = viewModel.fullViewPagerAdapter

        viewModel.imageChangeSubject.subscribe {
            Palette.from(it).generate().dominantSwatch?.let {
                binding.container.setBackgroundColor(it.rgb)
                binding.toolbar.setTitleTextColor(it.titleTextColor)
            }
        }.also { disposer.add(it) }

        viewModel.pageChangeSubject.subscribe {
            binding.fullViewPager.setCurrentItem(it, false)
        }.also { disposer.add(it) }

        viewModel.layoutChangeSubject.subscribe {
            val dragState = it.first
            val dy = it.third
            when (dragState) {
                FullViewViewModel.DragState.Dragging -> {
                    val top = binding.fullViewPager.top + dy
                    val bottom = top + binding.fullViewPager.height
                    binding.fullViewPager.layout(binding.fullViewPager.left, top.toInt(), binding.fullViewPager.right, bottom.toInt())
                }
                FullViewViewModel.DragState.Finish -> {
                    binding.fullViewPager.apply {
                        if (dy <= FINISH_DISTANCE) {
                            // 規定距離移動時、画面終了
                            finish()
                            return@subscribe
                        }
                        viewModel.playReturnLayoutAnimator(this, this.y, binding.container.y)
                    }
                }
            }

        }.also { disposer.add(it) }

        viewModel.favoriteChangeSubject.subscribe {
            favorite = it
            invalidateOptionsMenu()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (favorite) menuInflater.inflate(R.menu.full_view_unfavorite, menu) else menuInflater.inflate(R.menu.full_view_favorite, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.favorite -> {
                viewModel.changeFavorite()
            }
            R.id.save -> {
                val denied = checkSelfPermission(*Constants.PERMISSION_FILE_SAVE)
                when (denied.size) {
                    0 -> {
                        viewModel.saveImage()
                    }
                    else -> {
                        requestPermissions(Constants.PERMISSION_FILE_SAVE, REQUEST_PERMISSION)
                    }
                }
            }
            R.id.share -> {
                viewModel.shareImage()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            showSystemUI()
        }
    }

    override fun showSystemUI() {
        super.showSystemUI()
        binding.toolbar.visibility = View.VISIBLE
        binding.favoriteFloatingActionButton.show()
    }

    override fun hideSystemUI() {
        super.hideSystemUI()
        binding.toolbar.visibility = View.INVISIBLE
        binding.favoriteFloatingActionButton.hide()
    }

}