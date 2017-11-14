package ns.me.ns.furaffinity.ui.activity

import android.annotation.SuppressLint
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
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.graphics.Palette
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import javax.inject.Inject


class FullViewActivity : AbstractBaseActivity<FullViewViewModel>(), Injectable {

    companion object {

        private const val KEY_BUNDLE_VIEW_ID = "view_id"

        private const val KEY_BUNDLE_BITMP = "bitmp"

        private const val KEY_SHARED_IMAGE = "imageElement"

        fun intent(context: Context, viewId: Int, bitmp: Bitmap?) = Intent(context, FullViewActivity::class.java).apply {
            putExtra(KEY_BUNDLE_VIEW_ID, viewId)
            putExtra(KEY_BUNDLE_BITMP, bitmp)
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_view)
        setSupportActionBar(binding.toolbar)
        binding.fullImageView.setImageBitmap(intent.getParcelableExtra<Bitmap>(KEY_BUNDLE_BITMP))
        binding.viewModel = viewModel

        getImageBitmap()?.let {
            Palette.from(it).generate().dominantSwatch?.let {
                binding.container.setBackgroundColor(it.rgb)
                binding.toolbar.setBackgroundColor(it.rgb)
                binding.toolbar.setTitleTextColor(it.titleTextColor)
            }
        }

        binding.fullImageView.transitionName = KEY_SHARED_IMAGE
        binding.fullImageView.setOnClickListener {
            val visible = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
            if (visible) {
                hideSystemUI()
            } else {
                showSystemUI()
            }
        }
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
                            BitmapUtil.write(viewModel.viewId.toString(), it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        Snackbar.make(binding.root, R.string.message_save_image_success, Snackbar.LENGTH_SHORT).show()
                                    }, {
                                        LogUtil.e(it)
                                        Snackbar.make(binding.root, R.string.message_save_image_failed, Snackbar.LENGTH_SHORT).show()
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

    private fun getImageBitmap(): Bitmap? {
        return (binding.fullImageView.drawable as? BitmapDrawable)?.bitmap
    }

}