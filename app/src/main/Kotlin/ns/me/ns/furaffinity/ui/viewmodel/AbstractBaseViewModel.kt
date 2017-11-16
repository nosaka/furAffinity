package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Intent
import io.reactivex.subjects.PublishSubject


/**
 * 抽象基底ViewModel
 */
abstract class AbstractBaseViewModel(application: Application) : AndroidViewModel(application), ViewModelLifecycle {

    protected val context = application

    val startActivitySubject: PublishSubject<Intent> = PublishSubject.create()

    val finishActivitySubject: PublishSubject<Unit> = PublishSubject.create()

    val systemUISubject: PublishSubject<Unit> = PublishSubject.create()

    val executePendingBindingsSubject: PublishSubject<Unit> = PublishSubject.create()

}