package ns.me.ns.furaffinity.ui.activity

import android.animation.ValueAnimator
import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.graphics.Palette
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ns.me.ns.furaffinity.Constants
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.checkSelfPermission
import ns.me.ns.furaffinity.databinding.ActivityFullViewBinding
import ns.me.ns.furaffinity.di.Injectable
import ns.me.ns.furaffinity.ui.viewmodel.FullViewViewModel
import ns.me.ns.furaffinity.util.BitmapUtil
import ns.me.ns.furaffinity.util.LogUtil
import ns.me.ns.furaffinity.util.ToastUtil
import javax.inject.Inject


class FullViewActivity : AbstractBaseActivity<FullViewViewModel>(), Injectable {

    companion object {

        private const val KEY_BUNDLE_VIEW_ID = "view_id"

        private const val KEY_BUNDLE_BITMP = "bitmp"

        private const val KEY_SHARED_IMAGE = "imageElement"

        // Y軸最低スワイプ距離
        private const val FINISH_DISTANCE = -320

        fun intent(context: Context, viewId: Int, bitmap: Bitmap?) = Intent(context, FullViewActivity::class.java).apply {
            putExtra(KEY_BUNDLE_VIEW_ID, viewId)
            putExtra(KEY_BUNDLE_BITMP, bitmap)
        }

        fun option(activity: Activity, view: View): Bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, KEY_SHARED_IMAGE).toBundle()
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
        setSupportActionBar(binding.toolbar)
        getImageBitmap()?.let {
            Palette.from(it).generate().dominantSwatch?.let {
                binding.container.setBackgroundColor(it.rgb)
                binding.toolbar.setBackgroundColor(it.rgb)
                binding.toolbar.setTitleTextColor(it.titleTextColor)
            }
        }
        binding.fullImageView.setImageBitmap(intent.getParcelableExtra(KEY_BUNDLE_BITMP))
        binding.fullImageView.scaleType = ImageView.ScaleType.FIT_CENTER
        binding.fullImageView.transitionName = KEY_SHARED_IMAGE

        binding.fullImageView.apply {
            var animator: ValueAnimator? = null
            setOnViewDragListener { _, dy ->
                if (dy > 0) return@setOnViewDragListener
                animator?.cancel()
                val top = top + dy
                val bottom = top + height
                layout(left, top.toInt(), right, bottom.toInt())
            }

            setOnSingleFlingListener { _, _, _, _ ->
                animator?.cancel()
                if (this.y <= FINISH_DISTANCE) {
                    finish()
                    return@setOnSingleFlingListener true
                }
                animator = ValueAnimator.ofFloat(this.y, 0f)
                animator?.addUpdateListener { animation ->
                    (animation.animatedValue as? Float)?.let {
                        this.y = it
                    }
                }
                animator?.setTarget(this)
                animator?.start()
                return@setOnSingleFlingListener false
            }

        }
        binding.fullImageView.clearFocus()

        binding.viewModel = viewModel


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.full_view, menu)
        return true;
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save -> {
                val denied = checkSelfPermission(*Constants.PERMISSION_FILE_SAVE)
                when (denied.size) {
                    0 -> {
                        getImageBitmap()?.let {
                            item.isEnabled = false
                            BitmapUtil.write(viewModel.viewId.toString(), it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnComplete {
                                        item.isEnabled = true
                                    }
                                    .subscribe({
                                        ToastUtil.showToast(this, R.string.message_save_image_success)
                                    }, {
                                        LogUtil.e(it)
                                        ToastUtil.showToast(this, R.string.message_save_image_failed)
                                    })
                        }
                    }
                    else -> {
                        requestPermissions(Constants.PERMISSION_FILE_SAVE, REQUEST_PERMISSION)
                    }
                }
            }
            R.id.share -> {
                getImageBitmap()?.let {
                    val uri = MediaStore.Images.Media.insertImage(contentResolver, it, "name", null)
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_STREAM, Uri.parse(uri))
                            .setType("imageElement/jpeg"), getString(R.string.message_save_image_failed)))
                }
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

    private fun getImageBitmap(): Bitmap? {
        return (binding.fullImageView.drawable as? BitmapDrawable)?.bitmap
    }

}