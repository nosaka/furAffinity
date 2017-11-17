package ns.me.ns.furaffinity.di.modules

import dagger.Module
import dagger.Provides
import ns.me.ns.furaffinity.ds.local.dao.FavoriteDao
import ns.me.ns.furaffinity.pacakge.FavoriteLogic
import javax.inject.Singleton

/**
 * Logic Provider
 */
@Module
class LogicModule {

    @Provides
    @Singleton
    fun provideFavoriteLogic(favoriteDao: FavoriteDao): FavoriteLogic = FavoriteLogic(favoriteDao)

}