package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.ds.remote.AppWebApiService
import ns.me.ns.furaffinity.exception.LoginRequiredException
import ns.me.ns.furaffinity.repository.SubmissionRepository
import ns.me.ns.furaffinity.ui.activity.LoginActivity
import ns.me.ns.furaffinity.ui.adapter.AbstractRecyclerViewAdapter
import ns.me.ns.furaffinity.ui.adapter.OnEndScrollListener
import ns.me.ns.furaffinity.ui.adapter.recycler.SubmissionsAdapter
import ns.me.ns.furaffinity.util.LogUtil
import javax.inject.Inject

class SubmissionsViewModel @Inject constructor(application: Application) : AbstractBaseViewModel(application) {

    @Inject
    lateinit var service: AppWebApiService

    @Inject
    lateinit var submissionRepository: SubmissionRepository

    val adapterOnItemClickSubject: PublishSubject<AbstractRecyclerViewAdapter.OnClickItem<SubmissionsAdapter.ContentsViewModel>> = PublishSubject.create()

    val submissionsAdapter: SubmissionsAdapter by lazy {
        SubmissionsAdapter(application).apply {
            onItemClickPublishSubject.subscribe {
                adapterOnItemClickSubject.onNext(it)
            }
            onRequestReload = {
                getRemote()
            }
            setFooterDisplay(true)
        }
    }

    val onEndScrollListener = object : OnEndScrollListener() {
        override fun onEndScroll(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (isGetLocal) getRemote() else getLocal()
        }
    }

    private var isGetLocal = false

    private fun getLocal() {
        isGetLocal = true
        val lastViewId = submissionsAdapter.getData(submissionsAdapter.dataCount - 1)?.viewId
        submissionRepository.getLocal(lastViewId)
                .map {
                    return@map it.map {
                        SubmissionsAdapter.ContentsViewModel(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) {
                        submissionsAdapter.addDataAll(it)
                    } else {
                        getRemote()
                    }

                }, {
                    LogUtil.e(it)
                    submissionsAdapter.loadingError = true
                })

    }

    private fun getRemote() {
        submissionRepository.getRemote()
                .map {
                    return@map it.map {
                        SubmissionsAdapter.ContentsViewModel(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) {
                        submissionsAdapter.addDataAll(it)
                    } else {
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