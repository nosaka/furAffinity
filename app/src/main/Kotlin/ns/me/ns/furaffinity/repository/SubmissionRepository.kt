package ns.me.ns.furaffinity.repository

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import ns.me.ns.furaffinity.ds.dao.SubmissionDao
import ns.me.ns.furaffinity.ds.webapi.AppWebApiService
import ns.me.ns.furaffinity.repository.model.local.Submission
import ns.me.ns.furaffinity.repository.model.remote.entity.ViewElement
import javax.inject.Inject

/**
 * [Submission] Repository
 */
class SubmissionRepository @Inject constructor(private val service: AppWebApiService, private val submissionDao: SubmissionDao) {

    fun get(): Single<List<Submission>> {
        return Single.create<List<Submission>> { emitter ->
            val items = submissionDao.all()
            if (items.isNotEmpty()) {
                emitter.onSuccess(items)
            } else {
                service.getMsgSubmissions()
                        .map {
                            return@map it.viewElements.mapNotNull { convert(it) }
                        }
                        .subscribe({
                            submissionDao.insert(*it.toTypedArray())
                            emitter.onSuccess(it)
                        }, {
                            emitter.onError(it)
                        })

            }
        }
    }

    fun getLocal(): Single<List<Submission>> = Single.just(submissionDao.all())

    fun refresh(): Observable<List<Submission>> {

        return Observable.create<List<Submission>> { emitter ->
            val items = submissionDao.all()
            if (items.isEmpty()) {
                service.getMsgSubmissions()
                        .map {
                            return@map it.viewElements.mapNotNull { convert(it) }
                        }
                        .doOnSuccess {
                            submissionDao.insert(*it.toTypedArray())
                        }
                        .subscribe({
                            emitter.onNext(it)
                            emitter.onComplete()
                        }, {
                            emitter.onError(it)
                        })

            }
            val lastViewId = items.last().viewId
            getMsgSubmissionsUntilViewId(lastViewId, 0, emitter)

        }
    }

    private fun getMsgSubmissionsUntilViewId(untilViewId: Int, nextViewId: Int, emitter: ObservableEmitter<List<Submission>>) {
        service.getMsgSubmissions(nextViewId)
                .map {
                    return@map it.viewElements.mapNotNull { convert(it) }
                }
                .doOnSuccess {
                    submissionDao.insert(*it.toTypedArray())
                }
                .subscribe({
                    emitter.onNext(it)
                    val moreNextViewId = it.lastOrNull()?.viewId?.minus(1) ?: 0
                    if (it.isEmpty() || untilViewId >= it.lastOrNull()?.viewId ?: 0) {
                        emitter.onComplete()
                    } else {
                        getMsgSubmissionsUntilViewId(untilViewId, moreNextViewId, emitter)
                    }
                }, {
                    emitter.onError(it)
                })
    }

    fun getMore(): Single<List<Submission>> {
        return Single.create<List<Submission>> { emitter ->
            val items = submissionDao.all()
            val nextViewId = items.lastOrNull()?.viewId?.minus(1) ?: 0

            service.getMsgSubmissions(nextViewId)
                    .map {
                        return@map it.viewElements.mapNotNull { convert(it) }
                    }
                    .doOnSuccess {
                        submissionDao.insert(*it.toTypedArray())
                    }
                    .subscribe({
                        emitter.onSuccess(it)
                    }, {
                        emitter.onError(it)
                    })
        }
    }

    private fun convert(value: ViewElement): Submission? {
        val viewId = value.viewId ?: return null
        val entity = Submission()
        entity.viewId = viewId
        entity.name = value.name
        entity.src = value.imageElement.src
        entity.alt = value.imageElement.alt
        return entity
    }

}