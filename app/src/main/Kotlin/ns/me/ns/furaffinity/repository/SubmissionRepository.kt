package ns.me.ns.furaffinity.repository

import io.reactivex.Single
import ns.me.ns.furaffinity.ds.local.dao.SubmissionDao
import ns.me.ns.furaffinity.ds.local.model.Submission
import ns.me.ns.furaffinity.ds.remote.AppWebApiService
import javax.inject.Inject

/**
 * [Submission] Repository
 */
class SubmissionRepository @Inject constructor(private val service: AppWebApiService, private val submissionDao: SubmissionDao) {

    fun getLocal(viewId: Int?): Single<List<Submission>> = if (viewId != null) submissionDao.allThanViewId(viewId) else submissionDao.all()

    fun getRemote(): Single<List<Submission>> {
        var viewId = 0
        return submissionDao.all()
                .map { it.lastOrNull()?.viewId ?: 0 }
                .flatMap {
                    viewId = it
                    return@flatMap service.getMsgSubmissions(lastViewId = viewId)
                }
                .map {
                    return@map it.viewElements
                            .filter { it.viewId != viewId }
                            .map convert@ {
                                val entity = Submission()
                                entity.viewId = it.viewId!!
                                entity.name = it.name
                                entity.src = it.imageElement.src
                                entity.alt = it.imageElement.alt
                                return@convert entity
                            }
                }
                .doOnSuccess {
                    submissionDao.insert(*it.toTypedArray())
                }
    }
}