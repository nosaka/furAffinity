package ns.me.ns.furaffinity.repository

import io.reactivex.Single
import ns.me.ns.furaffinity.ds.dao.GalleryDao
import ns.me.ns.furaffinity.ds.webapi.AppWebApiService
import ns.me.ns.furaffinity.repository.model.local.Gallery
import ns.me.ns.furaffinity.repository.model.remote.entity.ViewElement
import javax.inject.Inject

/**
 * [Gallery] Repository
 */
class GalleryRepository @Inject constructor(private val service: AppWebApiService, private val galleryDao: GalleryDao) {

    fun get(account: String): Single<List<Gallery>> {
        return Single.create<List<Gallery>> { emitter ->
            val items = galleryDao.all(account)
            if (items.isNotEmpty()) {
                emitter.onSuccess(items)
            } else {
                service.getGallery(account)
                        .map {
                            return@map it.viewElements.mapNotNull { convert(it) }
                        }
                        .subscribe({
                            galleryDao.insert(*it.toTypedArray())
                            emitter.onSuccess(it)
                        }, {
                            emitter.onError(it)
                        })

            }
        }
    }

    fun getMore(account: String, page: Int): Single<List<Gallery>> {
        return Single.create<List<Gallery>> { emitter ->
            service.getGallery(account, page)
                    .map {
                        return@map it.viewElements.mapNotNull { convert(it) }
                    }
                    .doOnSuccess {
                        galleryDao.insert(*it.toTypedArray())
                    }
                    .subscribe({
                        emitter.onSuccess(it)
                    }, {
                        emitter.onError(it)
                    })
        }
    }

    fun getLocal(account: String): Single<List<Gallery>> = Single.just(galleryDao.all(account))

//    fun refresh(account: String): Observable<List<Gallery>> {
//        return Observable.create<List<Gallery>> { emitter ->
//            val items = galleryDao.all()
//            if (items.isEmpty()) {
//                service.getGallery(account, page = 1)
//                        .map {
//                            return@map it.viewElements.mapNotNull { convert(it) }
//                        }
//                        .doOnSuccess {
//                            galleryDao.insert(*it.toTypedArray())
//                        }
//                        .subscribe({
//                            emitter.onNext(it)
//                            emitter.onComplete()
//                        }, {
//                            emitter.onError(it)
//                        })
//
//            }
//            val lastViewId = items.last().viewId
//            getGalleryUntilViewId(account, lastViewId, 0, emitter)
//
//        }
//    }

//    private fun getGalleryUntilViewId(account: String, untilViewId: Int, nextViewId: Int, emitter: ObservableEmitter<List<Gallery>>) {
//        service.getGallery(account)
//                .map {
//                    return@map it.viewElements.mapNotNull { convert(it) }
//                }
//                .doOnSuccess {
//                    galleryDao.insert(*it.toTypedArray())
//                }
//                .subscribe({
//                    emitter.onNext(it)
//                    val moreNextViewId = it.lastOrNull()?.viewId?.minus(1) ?: 0
//                    if (it.isEmpty() || untilViewId >= it.lastOrNull()?.viewId ?: 0) {
//                        emitter.onComplete()
//                    } else {
//                        getMsgGallerysUntilViewId(untilViewId, moreNextViewId, emitter)
//                    }
//                }, {
//                    emitter.onError(it)
//                })
//    }
//


    private fun convert(value: ViewElement): Gallery? {
        val viewId = value.viewId ?: return null
        val entity = Gallery()
        entity.viewId = viewId
        entity.name = value.name
        entity.src = value.imageElement.src
        entity.alt = value.imageElement.alt
        entity.account = value.userElement.account
        return entity
    }

}