package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.ds.remote.AppWebApiService
import ns.me.ns.furaffinity.exception.LoginRequiredException
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.ui.adapter.OnEndScrollListener
import ns.me.ns.furaffinity.ui.adapter.recycler.SubmissionsAdapter
import ns.me.ns.furaffinity.util.LogUtil
import javax.inject.Inject

class SubmissionsViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var service: AppWebApiService

    private var pathMore: String? = ""

    val fullViewSubject: PublishSubject<Pair<View?, SubmissionsAdapter.ContentsViewModel>> = PublishSubject.create()

    val submissionsAdapter: SubmissionsAdapter by lazy {
        SubmissionsAdapter(application).apply {
            onItemClick = { _, data, view ->
                fullViewSubject.onNext(Pair(view, data))
            }
            onRequestReload = {
                getBrowse()
            }
            setFooterDisplay(true)
        }
    }

    val onEndScrollListener = object : OnEndScrollListener() {
        override fun onEndScroll(recyclerView: RecyclerView, dx: Int, dy: Int) {
            getBrowse()
        }
    }

    private fun getBrowse() {
        val more = pathMore
        if (more == null) {
            submissionsAdapter.setFooterDisplay(false)
            submissionsAdapter.notifyItemChanged(submissionsAdapter.itemCount)
            return
        }

        service.getMsgSubmissions(pathMore = more)
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    pathMore = it.pathMore
                }
                .map { response ->
                    return@map response.viewElements.map { element ->
                        SubmissionsAdapter.ContentsViewModel(element)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    submissionsAdapter.addDataAll(it)
                    if (it.isEmpty()) {
                        submissionsAdapter.setFooterDisplay(false)
                    }
                }, {
                    LogUtil.e(it)
                    submissionsAdapter.loadingError = true
                    if (it is LoginRequiredException) {
                        startActivitySubject.onNext(LoginActivity.intent(context))
                        finishActivitySubject.onNext(Unit)
                    }
                })
    }

}