package jp.kuaddo.tsuidezake.data.repository.internal.di

import dagger.Binds
import dagger.Module
import jp.kuaddo.tsuidezake.data.repository.internal.RepositoryImpl
import jp.kuaddo.tsuidezake.domain.Repository

@Module
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(impl: RepositoryImpl): Repository
}
