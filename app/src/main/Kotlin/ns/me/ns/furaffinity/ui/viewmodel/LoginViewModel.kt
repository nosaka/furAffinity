package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.databinding.ObservableField
import android.view.View
import javax.inject.Inject

class LoginViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    var mailAddressInput = ObservableField<String>()

    var passwordInput = ObservableField<String>()


    fun onClickLoginButton(view: View) {

        mailAddressInput.set("sfa")
        passwordInput.set("sss")

    }


}