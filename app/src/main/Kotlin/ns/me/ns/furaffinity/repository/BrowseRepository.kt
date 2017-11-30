package ns.me.ns.furaffinity.repository

import io.reactivex.Single
import ns.me.ns.furaffinity.ds.dao.BrowseDao
import ns.me.ns.furaffinity.ds.webapi.AppWebApiService
import ns.me.ns.furaffinity.repository.model.local.Browse
import ns.me.ns.furaffinity.repository.model.remote.entity.ViewElement
import javax.inject.Inject

/**
 * [Browse] Repository
 */
class BrowseRepository @Inject constructor(private val service: AppWebApiService, private val browseDao: BrowseDao) {

    fun get(): Single<List<Browse>> {
        return Single.create<List<Browse>> { emitter ->
            val items = browseDao.all()
            if (items.isNotEmpty()) {
                emitter.onSuccess(items)
            } else {
                service.getBrowse()
                        .map {
                            return@map it.viewElements.mapNotNull { convert(it) }
                        }
                        .subscribe({
                            browseDao.insert(*it.toTypedArray())
                            emitter.onSuccess(it)
                        }, {
                            emitter.onError(it)
                        })

            }
        }
    }

    fun getMore(page: Int): Single<List<Browse>> {
        return Single.create<List<Browse>> { emitter ->
            service.getBrowse(page)
                    .map {
                        return@map it.viewElements.mapNotNull { convert(it) }
                    }
                    .doOnSuccess {
                        browseDao.insert(*it.toTypedArray())
                    }
                    .subscribe({
                        emitter.onSuccess(it)
                    }, {
                        emitter.onError(it)
                    })
        }
    }

    fun refresh(): Single<List<Browse>> {
        return Single.create<List<Browse>> { emitter ->
            browseDao.deleteAll()
            service.getBrowse()
                    .map {
                        return@map it.viewElements.mapNotNull { convert(it) }
                    }
                    .doOnSuccess {
                        browseDao.insert(*it.toTypedArray())
                    }
                    .subscribe({
                        emitter.onSuccess(it)
                    }, {
                        emitter.onError(it)
                    })
        }
    }


    fun getLocal(): Single<List<Browse>> = Single.just(browseDao.all())

    private fun convert(value: ViewElement): Browse? {
        val viewId = value.viewId ?: return null
        val entity = Browse()
        entity.viewId = viewId
        entity.name = value.name
        entity.src = value.imageElement.src
        entity.alt = value.imageElement.alt
        entity.account = value.userElement.account
        return entity
    }
}