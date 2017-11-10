package ns.me.ns.furaffinity.ui.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.databinding.ActivityLoginBinding
import ns.me.ns.furaffinity.ui.viewmodel.LoginViewModel
import ns.me.ns.furaffinity.util.LoginManager

class LoginActivity : AbstractBaseActivity<LoginViewModel>() {

    companion object {
        fun intent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val cookie = CookieManager.getInstance().getCookie(url)
                LoginManager.instance.setCookie(this@LoginActivity, cookie)
            }
        }


        binding.webView.loadUrl("https://www.furaffinity.net/login")


    }


}