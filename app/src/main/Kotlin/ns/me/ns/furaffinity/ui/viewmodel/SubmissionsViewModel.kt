package ns.me.ns.furaffinity.ui.viewmodel

import android.app.Application
import android.databinding.ObservableField
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ns.me.ns.furaffinity.R
import ns.me.ns.furaffinity.exception.DisplayUIError
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
    lateinit var submissionRepository: SubmissionRepository

    val adapterOnItemClickSubject: PublishSubject<AbstractRecyclerViewAdapter.OnClickItem<SubmissionsAdapter.ContentsViewModel>> = PublishSubject.create()

    val submissionsAdapter: SubmissionsAdapter by lazy {
        SubmissionsAdapter(application).apply {
            onItemClickPublishSubject.subscribe {
                adapterOnItemClickSubject.onNext(it)
            }
            onRequestReload = {
                getSubmissions()
            }
            setFooterDisplay(true)
        }
    }

    val onEndScrollListener = object : OnEndScrollListener() {
        override fun onEndScroll(recyclerView: RecyclerView, dx: Int, dy: Int) {
            getSubmissions()
        }
    }

    val onRefreshListener = SwipeRefreshLayout.OnRefreshListener { refreshSubmissions() }

    val refreshing = ObservableField<Boolean>().apply { set(false) }

    private fun getSubmissions() {
        if (refreshing.get() == true) return

        val get = if (submissionsAdapter.dataCount <= 0) submissionRepository.get() else submissionRepository.getMore()
        get.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    return@map it.map {
                        SubmissionsAdapter.ContentsViewModel(it)
                    }
                }
                .subscribe({
                    submissionsAdapter.setLoadingError(false)
                    if (it.isNotEmpty()) {
                        submissionsAdapter.addDataAll(it)
                    } else {
                        submissionsAdapter.setFooterDisplay(false)
                    }
                }, {
                    LogUtil.e(it)
                    submissionsAdapter.setLoadingError(true)
                    if (it is LoginRequiredException) {
                        startActivitySubject.onNext(LoginActivity.intent(context))
                        finishActivitySubject.onNext(Unit)
                    }
                })

    }

    private fun refreshSubmissions() {
        refreshing.set(true)
        submissionsAdapter.setFooterDisplay(false)
        submissionRepository.refresh()
                .map {
                    return@map it.map {
                        SubmissionsAdapter.ContentsViewModel(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    submissionsAdapter.clear()
                }
                .subscribe({
                    submissionsAdapter.setLoadingError(false)
                    if (it.isNotEmpty()) {
                        submissionsAdapter.addDataAll(it)
                        submissionsAdapter.setFooterDisplay(true)
                    } else {
                        submissionsAdapter.setFooterDisplay(false)
                    }
                }, {
                    LogUtil.e(it)
                    if (it is LoginRequiredException) {
                        startActivitySubject.onNext(LoginActivity.intent(context))
                        finishActivitySubject.onNext(Unit)
                    }
                    refreshing.set(false)
                    displayUIErrorSubject.onNext(DisplayUIError(R.string.error_network))
                }, {
                    refreshing.set(false)
                })


    }

}