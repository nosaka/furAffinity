package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Intent
import android.view.View
import io.reactivex.subjects.PublishSubject


/**
 * 抽象基底ViewModel
 */
abstract class AbstractBaseViewModel(application: Application) : AndroidViewModel(application), ViewModelLifecycle {

    protected val context = application

    data class StartActivitySubject(val view: View?, val intent:Intent)

    val startActivitySubject: PublishSubject<StartActivitySubject> = PublishSubject.create()

    val executePendingBindingsSubject: PublishSubject<Unit> = PublishSubject.create()

}