package jp.kuaddo.tsuidezake.data.repository

import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.data.repository.internal.di.RepositorySubcomponent
import javax.inject.Singleton

@Module(subcomponents = [RepositorySubcomponent::class])
object RepositoryModule {
    @Provides
    @Singleton
    internal fun provideRepository(factory: RepositorySubcomponent.Factory): Repository {
        return factory.create().repositoryImpl()
    }
}
